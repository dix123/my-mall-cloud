package com.my.mall.shortlink.service.Impl;

import cn.hutool.core.annotation.Link;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.cache.constant.ShortLinkCache;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.common.core.api.ErrorCode;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.common.rocketmq.config.RocketMqConstant;
import com.my.mall.shortlink.constant.HttpConstant;
import com.my.mall.shortlink.constant.ShortLinkConstant;
import com.my.mall.shortlink.constant.ShortLinkGotoConstant;
import com.my.mall.shortlink.dto.req.ShortLinkCreateReqDTO;
import com.my.mall.shortlink.dto.req.ShortLinkStatsRecordDTO;
import com.my.mall.shortlink.dto.resp.ShortLinkCreateRespDTO;
import com.my.mall.shortlink.entity.LinkDO;
import com.my.mall.shortlink.entity.ShortLinkGotoDO;
import com.my.mall.shortlink.mapper.ShortLinkGotoMapper;
import com.my.mall.shortlink.mapper.ShortLinkMapper;
import com.my.mall.shortlink.service.ShortLinkService;
import com.my.mall.shortlink.util.HashUtil;
import com.my.mall.shortlink.util.LinkUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.checkerframework.checker.units.qual.C;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
    private RBloomFilter<String> shortUrlCreateBloomFilter;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RocketMQTemplate shortLinkStatusTemplate;


    @Override
    public List<GroupShortLinkCountDTO> listGroupShortLinkCount(List<String> gids) {
         return shortLinkMapper.listGroupShortLinkCount(gids);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO param) {
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
        stringRedisTemplate.opsForValue().set(
                String.format(ShortLinkGotoConstant.SHORT_LINK_GOTO_KEY, fullShortLink),
                param.getOriginUrl(),
                LinkUtil.getCacheTime(param.getValidDate())
        );
        shortUrlCreateBloomFilter.add(fullShortLink);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(HttpConstant.PREFIX + fullShortLink)
                .gid(param.getGid())
                .originalUrl(param.getOriginUrl())
                .build();
    }

    @Override
    @SneakyThrows(IOException.class)
    public void redirectUrl(String fullShortUrl, HttpServletRequest request, HttpServletResponse response) {
        String originalUrl = stringRedisTemplate.opsForValue().get(String.format(ShortLinkGotoConstant.SHORT_LINK_GOTO_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(originalUrl)) {
            shortLinkStatusCount(fullShortUrl, request, response);
            response.sendRedirect(originalUrl);
            return;
        }
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
                addResponseCookie(uvId, fullShortUrl, response);
            } else {
                uvId = cookie.getValue();
                Long add = stringRedisTemplate.opsForSet().add(String.format(ShortLinkCache.SHORT_LINK_UV_STATUS_KEY, fullShortUrl), uvId);
                uvFirstFlag = add !=null && add > 0;
            }
        } else {
            uvFirstFlag = true;
            addResponseCookie(uvId, fullShortUrl, response);
        }
        String ip = LinkUtil.getActualIp(request);
        String os = LinkUtil.getOs(request);
        String browser = LinkUtil.getBrowser(request);
        String network = LinkUtil.getNetwork(request);
        String device = LinkUtil.getDevice(request);
        Long uipAdded = stringRedisTemplate.opsForSet().add(String.format(ShortLinkCache.SHORT_LINK_UIP_STATUS_KEY, ip));
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

    private void addResponseCookie(String uvID, String fullShortUrl,  HttpServletResponse response) {
        uvID = UUID.fastUUID().toString();
        Cookie cookie = new Cookie(ShortLinkConstant.SHORT_LINK_UV_COOKIE_NAME, uvID);
        cookie.setMaxAge(ShortLinkConstant.SHORT_LINK_UV_COOKIE_LIVE_TIME);
        cookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf('/'), fullShortUrl.length()));
        response.addCookie(cookie);
        stringRedisTemplate.opsForSet().add(String.format(ShortLinkCache.SHORT_LINK_UV_STATUS_KEY, fullShortUrl), uvID);
    }


    @SneakyThrows
    private String getFavicon(String url) {
        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (HttpURLConnection.HTTP_OK == responseCode) {
            Document document = Jsoup.connect(url).get();
            Element faviconLink = document.select("link[rel~=(?i)^(shortcut )?icon]").first();
            if (faviconLink != null) {
                return faviconLink.attr("abs:href");
            }
        }
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
