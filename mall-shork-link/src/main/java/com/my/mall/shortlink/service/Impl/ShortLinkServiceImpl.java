package com.my.mall.shortlink.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.mall.common.core.constant.ShortLinkCache;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.api.shortlink.dto.req.*;
import com.my.mall.api.shortlink.dto.resp.ShortLinkBatchCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkGroupLinkCountQueryRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.common.core.api.ErrorCode;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.common.rocketmq.config.RocketMqConstant;
import com.my.mall.shortlink.config.WhiteListConfiguration;
import com.my.mall.shortlink.constant.HttpConstant;
import com.my.mall.shortlink.constant.ShortLinkConstant;
import com.my.mall.shortlink.dto.req.*;
import com.my.mall.common.data.entity.LinkDO;
import com.my.mall.shortlink.entity.ShortLinkGotoDO;
import com.my.mall.shortlink.enums.ValidTypeEnum;
import com.my.mall.shortlink.mapper.ShortLinkGotoMapper;
import com.my.mall.shortlink.mapper.ShortLinkMapper;
import com.my.mall.shortlink.service.ShortLinkService;
import com.my.mall.shortlink.util.HashUtil;
import com.my.mall.shortlink.util.LinkUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: haole
 * @Date: 2025/5/7
 **/
@Service
@Slf4j
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, LinkDO> implements ShortLinkService {

    @Autowired
    private ShortLinkMapper shortLinkMapper;
    @Autowired
    private ShortLinkGotoMapper shortLinkGotoMapper;
    @Value("${short-link.domain.default}")
    private String shortLinkDomain;
    @Autowired
    @Qualifier("shortUriCreateBloomFilter")
    private RBloomFilter<String> shortUrlCreateBloomFilter;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RocketMQTemplate shortLinkStatusTemplate;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private WhiteListConfiguration whiteListConfiguration;
    private final Random random = new Random();


    @Override
    public List<GroupShortLinkCountDTO> listGroupShortLinkCount(List<String> gids) {
         return shortLinkMapper.listGroupShortLinkCount(gids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO param) {
        verificationWhiteList(param.getOriginUrl());
//        if (originUrlBloomFilter.contains(param.getOriginUrl())) {
//            return ShortLinkCreateRespDTO.builder()
//                    .originalUrl(param.getOriginUrl())
//                    .fullShortUrl("数据库中已存在，请重新查询")
//                    .build();
//        }
        String suffix = generateSuffix(param);
        String fullShortLink = StrBuilder.create(HttpConstant.PREFIX + shortLinkDomain)
                .append("/")
                .append(suffix)
                .toString();
        LinkDO linkDO = LinkDO.builder()
                .domain(shortLinkDomain)
                .shortUri(suffix)
                .originUrl(param.getOriginUrl())
                .gid(param.getGid())
                .createdType(param.getCreatedType())
                .validDateType(param.getValidDateType())
                .validDate(param.getValidDate())
                .describe(param.getDescribe())
                .enableStatus(1)
                .totalPv(0)
                .totalUip(0)
                .totalUv(0)
                .delTime(0L)
                .fullShortUrl(fullShortLink)
                .favicon(getFavicon(param.getOriginUrl()))
                .build();
        linkDO.setCreateTime(LocalDateTime.now());
        linkDO.setUpdateTime(LocalDateTime.now());
        ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder()
                .fullShortUrl(fullShortLink)
                .gid(param.getGid())
                .build();
        try {
            baseMapper.insert(linkDO);
            shortLinkGotoMapper.insert(shortLinkGotoDO);
        } catch (DuplicateKeyException e) {
            if (!shortUrlCreateBloomFilter.contains(fullShortLink)) {
                shortUrlCreateBloomFilter.add(fullShortLink);
            }
            throw new ApiException(String.format("短连接：%s生成重复", fullShortLink));
        }
        int percent = random.nextInt(2,5);
        stringRedisTemplate.opsForValue().set(
                String.format(ShortLinkCache.SHORT_LINK_GOTO_KEY, fullShortLink),
                param.getOriginUrl(),
                LinkUtil.getCacheTime(param.getValidDate()) * percent / 10,
                TimeUnit.MINUTES
        );
        shortUrlCreateBloomFilter.add(fullShortLink);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(HttpConstant.PREFIX + fullShortLink)
                .gid(param.getGid())
                .originalUrl(param.getOriginUrl())
                .build();
    }

    private void verificationWhiteList(String originUrl) {
        Boolean enable = whiteListConfiguration.getEnable();
        if (BooleanUtil.isFalse(enable)) {
            return;
        }
        String fullHost = "";
        try {
            URI uri = new URI(originUrl);
            String host = uri.getHost();
            int port = uri.getPort();
            if (port != -1) {
                fullHost = host + ':' + port;
            } else {
                fullHost = host;
            }
        } catch (URISyntaxException e) {
            throw new ApiException("无效url");
        }
        List<String> whiteList = whiteListConfiguration.getDomain();
        if (!whiteList.contains(fullHost)) {
            throw new ApiException("请使用指定域名生成短链接");
        }
    }

    @Override
    @SneakyThrows(IOException.class)
    public void redirectUrl(String ShortUrl, HttpServletRequest request, HttpServletResponse response) {
        String fullShortUrl = StrBuilder.create(HttpConstant.PREFIX + shortLinkDomain)
                .append("/")
                .append(ShortUrl)
                .toString();
        log.info(fullShortUrl);
        String originalUrl = stringRedisTemplate.opsForValue().get(String.format(ShortLinkCache.SHORT_LINK_GOTO_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(originalUrl)) {
            shortLinkStatusCount(fullShortUrl, request, response);
            response.sendRedirect(originalUrl);
            return;
        }
        log.info("缓存没有");
        boolean contains = shortUrlCreateBloomFilter.contains(fullShortUrl);
        if (!contains) {
            log.info("布隆过滤器没有");
            throw new ApiException(ErrorCode.NULL_SHORT_URL);
        }
        Boolean hasMember = stringRedisTemplate.opsForSet().isMember(ShortLinkCache.SHORT_LINK_IS_NULL_KEY, fullShortUrl);
        if (BooleanUtil.isTrue(hasMember)) {
            log.info("null表有");
            throw new ApiException(ErrorCode.NULL_SHORT_URL);
        }
        RLock lock = redissonClient.getLock(String.format(ShortLinkCache.GO_TO_SHORT_LINK_LOCK, fullShortUrl));
        lock.lock();
        try {
            originalUrl = stringRedisTemplate.opsForValue().get(String.format(ShortLinkCache.SHORT_LINK_GOTO_KEY, fullShortUrl));
            if (StrUtil.isNotBlank(originalUrl)) {
                shortLinkStatusCount(fullShortUrl, request, response);
                response.sendRedirect(originalUrl);
                return;
            }
            Boolean member = stringRedisTemplate.opsForSet().isMember(ShortLinkCache.SHORT_LINK_IS_NULL_KEY, fullShortUrl);
            if (BooleanUtil.isTrue(member)) {
                throw new ApiException(ErrorCode.NULL_SHORT_URL);
            }
            LambdaQueryWrapper<ShortLinkGotoDO> wrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                    .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl)
                    .last("for update");
            ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(wrapper);
            if (shortLinkGotoDO == null) {
                stringRedisTemplate.opsForValue().set(String.format(ShortLinkCache.SHORT_LINK_IS_NULL_KEY, fullShortUrl), fullShortUrl, ShortLinkCache.NULL_KEY_OUT_TIME, TimeUnit.MINUTES);
                throw new ApiException(ErrorCode.NULL_SHORT_URL);
            }
            LambdaQueryWrapper<LinkDO> linkDoLambdaQueryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                    .eq(LinkDO::getGid, shortLinkGotoDO.getGid())
                    .eq(LinkDO::getFullShortUrl, fullShortUrl)
                    .eq(LinkDO::getEnableStatus, 1)
                    .ne(LinkDO::getDelFlag, 1)
                    .last("for update");
            List<LinkDO> linkDOList = shortLinkMapper.selectList(linkDoLambdaQueryWrapper);
            LinkDO linkDO = linkDOList.stream()
                    .filter(item -> item.getDelFlag().equals(0))
                    .findFirst().orElse(null);
            LinkDO moveLinkDO = linkDOList.stream()
                    .filter(item -> item.getDelFlag().equals(2))
                    .findFirst().orElse(null);
            // 知道迁移了，但是迁移可能还没完成
            if (linkDO == null && moveLinkDO != null) {
                while (true) {
                    Thread.sleep(50);
                    shortLinkGotoDO = shortLinkGotoMapper.selectOne(wrapper);
                    if (shortLinkGotoDO == null) {
                        stringRedisTemplate.opsForValue().set(String.format(ShortLinkCache.SHORT_LINK_IS_NULL_KEY, fullShortUrl), fullShortUrl, ShortLinkCache.NULL_KEY_OUT_TIME, TimeUnit.MINUTES);
                        throw new ApiException(ErrorCode.NULL_SHORT_URL);
                    }
                    linkDOList = shortLinkMapper.selectList(linkDoLambdaQueryWrapper);
                    linkDO = linkDOList.stream()
                            .filter(item -> item.getDelFlag().equals(0))
                            .findFirst().orElse(null);
                    if (linkDO != null) {
                        break;
                    }
                }
            }
            if (linkDO == null || (linkDO.getValidDate() != null && linkDO.getValidDate().isBefore(LocalDateTime.now()))) {
                stringRedisTemplate.opsForValue().set(String.format(ShortLinkCache.SHORT_LINK_IS_NULL_KEY, fullShortUrl), fullShortUrl, ShortLinkCache.NULL_KEY_OUT_TIME, TimeUnit.MINUTES);
                throw new ApiException(ErrorCode.NULL_SHORT_URL);
            }
            int percent = random.nextInt(2,5);
            stringRedisTemplate.opsForValue().set(String.format(ShortLinkCache.SHORT_LINK_GOTO_KEY, fullShortUrl), linkDO.getOriginUrl(), LinkUtil.getCacheTime(linkDO.getValidDate()) * percent / 10, TimeUnit.MINUTES);
            shortLinkStatusCount(fullShortUrl, request, response);
            response.sendRedirect(linkDO.getOriginUrl());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public ShortLinkBatchCreateRespDTO batchCreateShortLink(ShortLinkBatchCreateReqDTO param) {
        List<String> originUrls = param.getOriginUrls();
        List<String> describe = param.getDescribes();
        List<ShortLinkBatchCreateInsideRespDTO> result = new ArrayList<>();
        for (int i = 0; i < originUrls.size(); i++) {
            ShortLinkCreateReqDTO bean = BeanUtil.toBean(param, ShortLinkCreateReqDTO.class);
            bean.setOriginUrl(originUrls.get(i));
            bean.setDescribe(describe.get(i));
            ShortLinkCreateRespDTO shortLink = ((ShortLinkService) AopContext.currentProxy()).createShortLink(bean);
            ShortLinkBatchCreateInsideRespDTO build = ShortLinkBatchCreateInsideRespDTO.builder()
                    .shortUrl(shortLink.getFullShortUrl())
                    .originUrl(shortLink.getOriginalUrl())
                    .describe(describe.get(i))
                    .build();
            result.add(build);
        }
        return ShortLinkBatchCreateRespDTO.builder()
                .total(result.size())
                .infos(result)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShortLink(ShortLinkUpdateReqDTO param) {
        verificationWhiteList(param.getOriginUrl());
        LambdaQueryWrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getFullShortUrl, param.getFullShortUrl())
                .eq(LinkDO::getGid, param.getOriginGid())
                .eq(LinkDO::getDelFlag, 0);
        LinkDO linkDO = baseMapper.selectOne(wrapper);
        if (linkDO == null) {
            throw new ApiException("短链接不存在");
        }
        if (Objects.equals(param.getGid(), param.getOriginGid())) {
            LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                    .eq(LinkDO::getFullShortUrl, param.getFullShortUrl())
                    .eq(LinkDO::getGid, param.getGid())
                    .eq(LinkDO::getDelFlag, 0);
            LinkDO updateLinkDO = LinkDO.builder()
                    .domain(shortLinkDomain)
                    .shortUri(linkDO.getShortUri())
                    .fullShortUrl(linkDO.getFullShortUrl())
                    .gid(param.getGid())
                    .favicon(Objects.equals(param.getOriginUrl(), linkDO.getOriginUrl()) ? linkDO.getFavicon() : getFavicon(param.getOriginUrl()))
                    .createdType(linkDO.getCreatedType())
                    .describe(param.getDescribe())
                    .validDateType(param.getValidDateType())
                    .validDate(param.getValidDate())
                    .build();
            baseMapper.update(updateLinkDO, updateWrapper);
        } else {
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(ShortLinkCache.GID_UPDATE_LOCK_KEY, param.getFullShortUrl()));
            RLock lock = readWriteLock.writeLock();
            lock.lock();
            try {
                LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                        .eq(LinkDO::getFullShortUrl, param.getFullShortUrl())
                        .eq(LinkDO::getGid, param.getOriginGid())
                        .eq(LinkDO::getDelFlag, 0);
                LinkDO updateLinkDO = LinkDO.builder()
                        .delTime(System.currentTimeMillis())
                        .build();
                updateLinkDO.setDelFlag(2);
                baseMapper.update(updateLinkDO, updateWrapper);
                LinkDO shortLinkDO = LinkDO.builder()
                        .domain(shortLinkDomain)
                        .originUrl(param.getOriginUrl())
                        .gid(param.getGid())
                        .createdType(linkDO.getCreatedType())
                        .validDateType(param.getValidDateType())
                        .validDate(param.getValidDate())
                        .describe(param.getDescribe())
                        .shortUri(linkDO.getShortUri())
                        .enableStatus(linkDO.getEnableStatus())
                        .totalPv(linkDO.getTotalPv())
                        .totalUv(linkDO.getTotalUv())
                        .totalUip(linkDO.getTotalUip())
                        .fullShortUrl(linkDO.getFullShortUrl())
                        .favicon(Objects.equals(linkDO.getOriginUrl(), param.getOriginUrl()) ? linkDO.getFavicon() : getFavicon(param.getOriginUrl()))
                        .delTime(0L)
                        .build();
                shortLinkDO.setCreateTime(LocalDateTime.now());
                shortLinkDO.setUpdateTime(LocalDateTime.now());
                baseMapper.insert(shortLinkDO);
                LambdaQueryWrapper<ShortLinkGotoDO> gotoQueryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                        .eq(ShortLinkGotoDO::getFullShortUrl, param.getFullShortUrl());
                shortLinkGotoMapper.delete(gotoQueryWrapper);
                ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder()
                        .fullShortUrl(param.getFullShortUrl())
                        .gid(param.getGid())
                        .build();
                shortLinkGotoMapper.insert(shortLinkGotoDO);
            } finally {
                lock.unlock();
            }
        }
        if (!Objects.equals(param.getOriginUrl(), linkDO.getOriginUrl())
        || !Objects.equals(param.getValidDateType(), linkDO.getValidDateType())
        || !Objects.equals(param.getValidDate(), linkDO.getValidDate())) {
            stringRedisTemplate.delete(String.format(ShortLinkCache.SHORT_LINK_GOTO_KEY, param.getFullShortUrl()));
            LocalDateTime localDateTime = LocalDateTime.now();
            if (linkDO.getValidDate() != null && linkDO.getValidDate().isBefore(localDateTime)) {
                if (Objects.equals(param.getValidDateType(), ValidTypeEnum.PERMANENT.getValue()) || param.getValidDate().isAfter(localDateTime)) {
                    stringRedisTemplate.delete(String.format(ShortLinkCache.SHORT_LINK_IS_NULL_KEY, param.getFullShortUrl()));
                }
            }
        }
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPagesReqDto param) {
        IPage<LinkDO> linkDOIPage = baseMapper.pageShortLink(param);
        return linkDOIPage.convert(each ->{
            ShortLinkPageRespDTO rs = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
            rs.setDomain(HttpConstant.PREFIX + rs.getDomain());
            return rs;
        });
    }

    @Override
    public List<ShortLinkGroupLinkCountQueryRespDTO> countShortLink(List<String> gidList) {
        QueryWrapper<LinkDO> wrapper = Wrappers.query(new LinkDO())
                .select("gid, count(*) as count")
                .eq("enable_status", 1)
                .eq("del_flag", 0)
                .in("gid", gidList)
                .groupBy("gid");
        List<Map<String, Object>> maps = baseMapper.selectMaps(wrapper);
        return BeanUtil.copyToList(maps, ShortLinkGroupLinkCountQueryRespDTO.class);
    }


    private void shortLinkStatusCount(String fullShortUrl, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean uvFirstFlag = false;
        String uvId = "";
        if (ArrayUtil.isNotEmpty(cookies)) {
            Cookie cookie = Arrays.stream(cookies)
                    .filter(each -> ShortLinkConstant.SHORT_LINK_UV_COOKIE_NAME.equals(each.getName()))
                    .findFirst()
                    .orElseGet(() -> null);
            if (cookie == null) {
                uvFirstFlag = true;
                uvId = addResponseCookie(fullShortUrl, response);
            } else {
                uvId = cookie.getValue();
                Long add = stringRedisTemplate.opsForSet().add(String.format(ShortLinkCache.SHORT_LINK_UV_STATUS_KEY, fullShortUrl), uvId);
                uvFirstFlag = add !=null && add > 0;
            }
        } else {
            uvFirstFlag = true;
            uvId = addResponseCookie(fullShortUrl, response);
        }
        String ip = LinkUtil.getActualIp(request);
        String os = LinkUtil.getOs(request);
        String browser = LinkUtil.getBrowser(request);
        String network = LinkUtil.getNetwork(request);
        String device = LinkUtil.getDevice(request);
        Long uipAdded = stringRedisTemplate.opsForSet().add(String.format(ShortLinkCache.SHORT_LINK_UIP_STATUS_KEY, fullShortUrl), ip);
        boolean uipFirstFlag = uipAdded != null && uipAdded > 0L;
        ShortLinkStatsRecordDTO shortLinkStatsRecordDTO = ShortLinkStatsRecordDTO.builder()
                .fullShortUrl(fullShortUrl)
                .uipFirstFlag(uipFirstFlag)
                .uv(uvId)
                .uvFirstFlag(uvFirstFlag)
                .os(os)
                .ip(ip)
                .browser(browser)
                .network(network)
                .device(device)
                .currentDate(LocalDateTime.now())
                .build();
        SendStatus sendStatus = shortLinkStatusTemplate.syncSend(RocketMqConstant.SHORT_LINK_STATUS_TOPIC, new GenericMessage<>(shortLinkStatsRecordDTO)).getSendStatus();
        if (!Objects.equals(sendStatus, SendStatus.SEND_OK)) {
            throw new ApiException(ErrorCode.SERVICE_ERROR);
        }
    }

    private String addResponseCookie(String fullShortUrl,  HttpServletResponse response) {
        String uvID = UUID.fastUUID().toString();
        Cookie cookie = new Cookie(ShortLinkConstant.SHORT_LINK_UV_COOKIE_NAME, uvID);
        cookie.setMaxAge(ShortLinkConstant.SHORT_LINK_UV_COOKIE_LIVE_TIME);
        cookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.lastIndexOf('/'), fullShortUrl.length()));
        response.addCookie(cookie);
        stringRedisTemplate.opsForSet().add(String.format(ShortLinkCache.SHORT_LINK_UV_STATUS_KEY, fullShortUrl), uvID);
        return uvID;
    }


//    @SneakyThrows
//    private String getFavicon(String url) {
//        URL targetUrl = new URL(url);
//        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
//        connection.setRequestMethod("GET");
//        connection.connect();
//        int responseCode = connection.getResponseCode();
//        if (HttpURLConnection.HTTP_OK == responseCode) {
//            Document document = Jsoup.connect(url).get();
//            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
//            if (faviconLink != null) {
//                return faviconLink.attr("abs:href");
//            }
//        }
//        return null;
//    }

        private String getFavicon(String url) {
        // 验证URL合法性
        if (url == null || !url.startsWith("http")) {
            log.info("无效的URL: {}", url);
            return null;
        }
        try {
            // 使用Jsoup进行连接，统一处理
            Document document = Jsoup.connect(url)
                    .timeout(5000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .followRedirects(true)
                    .get();
            // 查找favicon链接
            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
            if (faviconLink != null) {
                String faviconUrl = faviconLink.attr("abs:href");
                log.debug("找到favicon: {}", faviconUrl);
                return faviconUrl;
            }
            // 如果页面中没有明确的favicon链接，尝试默认路径
            URL baseUrl = new URL(url);
            String defaultFavicon = baseUrl.getProtocol() + "://" + baseUrl.getHost() + "/favicon.ico";
            log.debug("尝试默认favicon路径: {}", defaultFavicon);
            // 检查默认favicon是否存在
            HttpURLConnection connection = (HttpURLConnection) new URL(defaultFavicon).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return defaultFavicon;
            }
        } catch (SocketTimeoutException e) {
            log.warn("获取favicon超时 - URL: {}", url, e);
        } catch (IOException e) {
            log.error("获取favicon时发生IO异常 - URL: {}", url, e);
        } catch (Exception e) {
            log.error("获取favicon时发生意外错误 - URL: {}", url, e);
        }
        // 如果所有尝试都失败，返回默认图标
        return null;
    }

    private String generateSuffix(ShortLinkCreateReqDTO param) {
        int generateCount = 0;
        String suffix = null;
        while (true) {
            if (generateCount > 10) {
                throw new ApiException("短链生成频繁");
            }
            String originalUrl = param.getOriginUrl();
            originalUrl += UUID.randomUUID().toString();
            suffix = HashUtil.hashToBase62(originalUrl);
            if (!shortUrlCreateBloomFilter.contains(HttpConstant.PREFIX + shortLinkDomain + "/" + suffix)) {
                break;
            }
            generateCount++;
        }
        return suffix;
    }
}
