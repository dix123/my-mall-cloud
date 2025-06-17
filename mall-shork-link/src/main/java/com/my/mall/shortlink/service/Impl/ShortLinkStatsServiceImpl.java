package com.my.mall.shortlink.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkAccessRecordRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkBrowserStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkDailyStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkDeviceStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkIpStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkLocalStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkNetworkStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkOsStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkUvStatsRespDTO;
import com.my.mall.shortlink.bo.*;
import com.my.mall.api.shortlink.dto.req.ShortLinkAccessRecordReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkGroupAccessRecordReqDTO;
import com.my.mall.api.shortlink.dto.req.entity.LinkAccessLogsDO;
import com.my.mall.shortlink.entity.LinkStatsTodayDO;
import com.my.mall.shortlink.mapper.*;
import com.my.mall.shortlink.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.my.mall.common.core.constant.CommonConstant.ONE_DAY_HOURS;
import static com.my.mall.common.core.constant.CommonConstant.ONE_WEEK_DAY;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/28
 **/
@Service
@RequiredArgsConstructor
public class ShortLinkStatsServiceImpl implements ShortLinkStatsService {

    private final LinkStatsTodayMapper linkStatsTodayMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final LinkLocalStatsMapper linkLocalStatsMapper;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkOsStatsMapper linkOsStatsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkNetworkStatsMapper linkNetworkStatsMapper;


    @Override
    public ShortLinkStatsRespDTO getShortLinkStats(ShortLinkStatsReqDTO param) {
        return getStats(param,
                linkStatsTodayMapper::listStatsByShortLink,
                linkAccessLogsMapper::listSumStatsByShortLink,
                linkLocalStatsMapper::listLocalStatsByShortLink,
                linkAccessStatsMapper::listHourStats,
                linkAccessLogsMapper::listIpStats,
                linkAccessStatsMapper::listWeekDayStatsByShortLink,
                linkBrowserStatsMapper::listBrowserStatsByShortLink,
                linkOsStatsMapper::listOsStatsByShortLink,
                linkAccessLogsMapper::listUvTypeStatsByShortLink,
                linkDeviceStatsMapper::listDeviceStatsByShortLink,
                linkNetworkStatsMapper::listNetworkStatsByShortLink
        );
    }

    @Override
    public ShortLinkStatsRespDTO getShortLinkStatsByGroup(ShortLinkStatsReqDTO param) {
        return getStats(
                param,
                linkStatsTodayMapper::listStatsByGroup,
                linkAccessLogsMapper::listSumStatsByGroup,
                linkLocalStatsMapper::listLocalStatsByGroup,
                linkAccessStatsMapper::listHourStatsByGroup,
                linkAccessLogsMapper::listIpStatsByGroup,
                linkAccessStatsMapper::listWeekDayStatsByGroup,
                linkBrowserStatsMapper::listBrowserStatsByGroup,
                linkOsStatsMapper::listOsStatsByGroup,
                linkAccessLogsMapper::listUvTypeStatsByGroup,
                linkDeviceStatsMapper::listDeviceStatsByGroup,
                linkNetworkStatsMapper::listNetworkStatsByGroup
        );
    }

    @Override
    public IPage<ShortLinkAccessRecordRespDTO> getShortLinkAccessRecordRespDTO(ShortLinkAccessRecordReqDTO param) {
        LambdaQueryWrapper<LinkAccessLogsDO> wrapper = Wrappers.lambdaQuery(LinkAccessLogsDO.class)
                .eq(LinkAccessLogsDO::getFullShortUrl, param.getFullShortUrl())
                .between(LinkAccessLogsDO::getCreateTime, param.getStartDate(), param.getEndDate())
                .eq(LinkAccessLogsDO::getDelFlag, 0)
                .orderByDesc(LinkAccessLogsDO::getCreateTime);
        IPage<LinkAccessLogsDO> linkAccessLogsDOIPage = linkAccessLogsMapper.selectPage(param, wrapper);
        IPage<ShortLinkAccessRecordRespDTO> rs = linkAccessLogsDOIPage.convert(each -> BeanUtil.toBean(each, ShortLinkAccessRecordRespDTO.class));
        Set<String> userSet = rs.getRecords().stream()
                .map(ShortLinkAccessRecordRespDTO::getUser)
                .collect(Collectors.toSet());
        List<UserTypeBO> userTypeList = linkAccessLogsMapper.getUserType(param.getFullShortUrl(), param.getStartDate(), param.getEndDate(), userSet);
        Map<String, String> userTypeMap = userTypeList.stream()
                .collect(Collectors.toMap(
                        UserTypeBO::getUser,
                        UserTypeBO::getType,
                        (existing, replacement) -> replacement
                ));
        rs.getRecords().forEach(each -> {
            each.setUvType(userTypeMap.get(each.getUser()));
        });
        return rs;
    }

    @Override
    public IPage<ShortLinkAccessRecordRespDTO> getGroupShortLinkAccessRecord(ShortLinkGroupAccessRecordReqDTO param) {
        IPage<LinkAccessLogsDO> linkAccessLogsPage = linkAccessLogsMapper.getAccessLogByGroup(param);
        IPage<ShortLinkAccessRecordRespDTO> rs = linkAccessLogsPage.convert(each -> BeanUtil.toBean(each, ShortLinkAccessRecordRespDTO.class));
        Set<String> userSet = rs.getRecords().stream()
                .map(ShortLinkAccessRecordRespDTO::getUser)
                .collect(Collectors.toSet());
        List<UserTypeBO> userTypeList = linkAccessLogsMapper.getGroupUserType(param.getGid(), param.getStartDate(), param.getEndDate(), userSet);
        Map<String, String> userTypeMap = userTypeList.stream()
                .collect(Collectors.toMap(
                        UserTypeBO::getUser,
                        UserTypeBO::getType,
                        (existing, replacement) -> replacement
                ));
        rs.getRecords().forEach(each -> {
            each.setUvType(userTypeMap.get(each.getUser()));
        });
        return rs;
    }

    public ShortLinkStatsRespDTO getStats(
            ShortLinkStatsReqDTO param,
            Function<ShortLinkStatsReqDTO, List<LinkStatsTodayDO>> dailyStatsProvider,
            Function<ShortLinkStatsReqDTO, ShortLinkSumStatsBO> shortLinkSumStatsProvider,
            Function<ShortLinkStatsReqDTO, List<ShortLinkLocalStatsBO>> shortLinkLocalStatsProvider,
            Function<ShortLinkStatsReqDTO, List<ShortLinkHourStatsBO>> shortLinkHourStatsProvider,
            Function<ShortLinkStatsReqDTO, List<ShortLinkIpStatsRespDTO>> shortLinkIpStatsProvider,
            Function<ShortLinkStatsReqDTO, List<ShortLinkWeekDayStatsBO>> shortLinkWeekDayStatsProvider,
            Function<ShortLinkStatsReqDTO, List<ShortLinkBrowserStatsBO>> shortLinkBrowserStatsProvider,
            Function<ShortLinkStatsReqDTO, List<ShortLinkOsStatsBO>> shortLinkOsStatsProvider,
            Function<ShortLinkStatsReqDTO, ShortLinkUvTypeBO> shortLinkUvTypeStatsProvider,
            Function<ShortLinkStatsReqDTO, List<ShortLinkDeviceStatsRespDTO>> shortLinkDeviceStatsProvider,
            Function<ShortLinkStatsReqDTO, List<ShortLinkNetworkStatsRespDTO>> shortLinkNetworkStatsProvider) {
        List<LinkStatsTodayDO> linkStatsTodayDOList = dailyStatsProvider.apply(param);
        ShortLinkSumStatsBO shortLinkSumStatsBO = shortLinkSumStatsProvider.apply(param);
        return ShortLinkStatsRespDTO.builder()
                .pv(shortLinkSumStatsBO.getPv())
                .uv(shortLinkSumStatsBO.getUv())
                .uip(shortLinkSumStatsBO.getUip())
                .daily(buildDailyStats(linkStatsTodayDOList, param))
                .localeCnStats(buildLocalStats(shortLinkLocalStatsProvider, param))
                .hourStats(buildHourStats(shortLinkHourStatsProvider, param))
                .topIpStats(buildIpStats(shortLinkIpStatsProvider, param))
                .weekdayStats(buildWeekDayStats(shortLinkWeekDayStatsProvider, param))
                .browserStats(buildBrowserStats(shortLinkBrowserStatsProvider, param))
                .osStats(buildOsStats(shortLinkOsStatsProvider, param))
                .uvTypeStats(buildUvTypeStats(shortLinkUvTypeStatsProvider, param))
                .deviceStats(buildDeviceStats(shortLinkDeviceStatsProvider, param))
                .networkStats(buildNetworkStats(shortLinkNetworkStatsProvider, param))
                .build();
    }

    private List<ShortLinkNetworkStatsRespDTO> buildNetworkStats(Function<ShortLinkStatsReqDTO, List<ShortLinkNetworkStatsRespDTO>> shortLinkNetworkStatsProvider, ShortLinkStatsReqDTO param) {
        List<ShortLinkNetworkStatsRespDTO> shortLinkNetworkStatsRespDTOList = shortLinkNetworkStatsProvider.apply(param);
        int sum = shortLinkNetworkStatsRespDTOList.stream()
                .mapToInt(ShortLinkNetworkStatsRespDTO::getCnt)
                .sum();
        return shortLinkNetworkStatsRespDTOList.stream()
                .peek(each -> {
                    double ratio = (double) each.getCnt() / sum;
                    double actualRatio = Math.round(ratio * 100) / 100.0;
                    each.setRatio(actualRatio);
                }).collect(Collectors.toList());
    }

    private List<ShortLinkDeviceStatsRespDTO> buildDeviceStats(Function<ShortLinkStatsReqDTO, List<ShortLinkDeviceStatsRespDTO>> shortLinkDeviceStatsProvider, ShortLinkStatsReqDTO param) {
        List<ShortLinkDeviceStatsRespDTO> shortLinkDeviceStatsRespDTOList = shortLinkDeviceStatsProvider.apply(param);
        int sum = shortLinkDeviceStatsRespDTOList.stream()
                        .mapToInt(ShortLinkDeviceStatsRespDTO::getCnt)
                                .sum();
        return shortLinkDeviceStatsRespDTOList.stream()
                .peek(each -> {
                    double ratio = (double) each.getCnt() / sum;
                    double actualRatio = Math.round(ratio * 100) / 100.0;
                    each.setRatio(actualRatio);
                }).collect(Collectors.toList());
    }

    private List<ShortLinkUvStatsRespDTO> buildUvTypeStats(Function<ShortLinkStatsReqDTO, ShortLinkUvTypeBO> shortLinkUvTypeStatsProvider, ShortLinkStatsReqDTO param) {
        ShortLinkUvTypeBO shortLinkUvTypeBO = shortLinkUvTypeStatsProvider.apply(param);
        List<ShortLinkUvStatsRespDTO> rs = new ArrayList<>();
        int sum = shortLinkUvTypeBO.getNewUserCount() + shortLinkUvTypeBO.getOldUserCount();
        double newUserRatio = (double) shortLinkUvTypeBO.getNewUserCount() / sum;
        double actualNewUserRatio = Math.round(newUserRatio * 100) / 100.0;
        double oleUserRatio = (double) shortLinkUvTypeBO.getOldUserCount() / sum;
        double actualOldUserRatio = Math.round(oleUserRatio * 100) / 100.0;
        rs.add(ShortLinkUvStatsRespDTO.builder()
                .uvType("newUser")
                .cnt(shortLinkUvTypeBO.getNewUserCount())
                .ratio(actualNewUserRatio)
                .build()
        );
        rs.add(ShortLinkUvStatsRespDTO.builder()
                .uvType("oldUser")
                .cnt(shortLinkUvTypeBO.getOldUserCount())
                .ratio(actualOldUserRatio)
                .build()
        );
        return rs;
    }

    private List<ShortLinkOsStatsRespDTO> buildOsStats(Function<ShortLinkStatsReqDTO, List<ShortLinkOsStatsBO>> shortLinkOsStatsProvider, ShortLinkStatsReqDTO param) {
        List<ShortLinkOsStatsBO> shortLinkOsStatsBOList = shortLinkOsStatsProvider.apply(param);
        int sum = shortLinkOsStatsBOList.stream()
                .mapToInt(ShortLinkOsStatsBO::getCnt)
                .sum();
        return shortLinkOsStatsBOList.stream()
                .map(each -> {
                    ShortLinkOsStatsRespDTO bean = BeanUtil.toBean(each, ShortLinkOsStatsRespDTO.class);
                    double ratio = (double) bean.getCnt() / sum;
                    double actualRatio = Math.round(ratio * 100) / 100.0;
                    bean.setRatio(actualRatio);
                    return bean;
                }).collect(Collectors.toList());
    }

    private List<ShortLinkBrowserStatsRespDTO> buildBrowserStats(Function<ShortLinkStatsReqDTO, List<ShortLinkBrowserStatsBO>> shortLinkBrowserStatsProvider, ShortLinkStatsReqDTO param) {
        List<ShortLinkBrowserStatsBO> shortLinkBrowserStatsBOList = shortLinkBrowserStatsProvider.apply(param);
        int sum = shortLinkBrowserStatsBOList.stream()
                        .mapToInt(ShortLinkBrowserStatsBO::getCnt)
                                .sum();
        return shortLinkBrowserStatsBOList.stream().map(each -> {
            ShortLinkBrowserStatsRespDTO bean = BeanUtil.toBean(each, ShortLinkBrowserStatsRespDTO.class);
            double ratio = (double) each.getCnt() / sum;
            double actualRatio = Math.round(ratio * 100) / 100.0;
            bean.setRatio(actualRatio);
            return bean;
        }).collect(Collectors.toList());
    }

    private List<Integer> buildWeekDayStats(Function<ShortLinkStatsReqDTO, List<ShortLinkWeekDayStatsBO>> shortLinkWeekDayStatsProvider, ShortLinkStatsReqDTO param) {
        List<ShortLinkWeekDayStatsBO> shortLinkWeekDayStatsBOList = shortLinkWeekDayStatsProvider.apply(param);
        Map<Integer, Integer> weekDayMap = shortLinkWeekDayStatsBOList.stream().collect(Collectors.toMap(
                ShortLinkWeekDayStatsBO::getWeekday,
                ShortLinkWeekDayStatsBO::getCount,
                (existing, replacement) -> replacement
        ));
        List<Integer> rs = new ArrayList<>();
        for (int i = 1; i < ONE_WEEK_DAY; i++) {
            rs.add(weekDayMap.getOrDefault(i, 0));
        }
        return rs;
    }

    private List<ShortLinkIpStatsRespDTO> buildIpStats(Function<ShortLinkStatsReqDTO, List<ShortLinkIpStatsRespDTO>> shortLinkIpStatsProvider, ShortLinkStatsReqDTO param) {
        return shortLinkIpStatsProvider.apply(param);
    }

    private List<Integer> buildHourStats(Function<ShortLinkStatsReqDTO, List<ShortLinkHourStatsBO>> shortLinkHourStatsProvider, ShortLinkStatsReqDTO param) {
        List<ShortLinkHourStatsBO> shortLinkHourStatsBOList = shortLinkHourStatsProvider.apply(param);
        Map<Integer, Integer> hourMap = shortLinkHourStatsBOList.stream()
                .collect(Collectors.toMap(
                        ShortLinkHourStatsBO::getHour,
                        ShortLinkHourStatsBO::getPv,
                        (existing, replacement) -> replacement
                ));
        int[] rs = new int[24];
        for (int i = 0; i < ONE_DAY_HOURS; i++) {
            rs[i] = hourMap.getOrDefault(i, 0);
        }
        return Arrays.stream(rs)
                .boxed()
                .collect(Collectors.toList());
    }

    private List<ShortLinkLocalStatsRespDTO> buildLocalStats(Function<ShortLinkStatsReqDTO, List<ShortLinkLocalStatsBO>> shortLinkLocalStatsProvider, ShortLinkStatsReqDTO param) {
        List<ShortLinkLocalStatsRespDTO> rs = new ArrayList<>();
        List<ShortLinkLocalStatsBO> shortLinkLocalStatsBOList = shortLinkLocalStatsProvider.apply(param);
        int sum = shortLinkLocalStatsBOList.stream().mapToInt(ShortLinkLocalStatsBO::getCnt).sum();
        shortLinkLocalStatsBOList.forEach(each -> {
            double ratio = (double) each.getCnt() / sum;
            double actualRatio = Math.round(ratio * 100) / 100.0;
            ShortLinkLocalStatsRespDTO shortLinkLocalStatsRespDTO = ShortLinkLocalStatsRespDTO.builder()
                    .local(each.getProvince())
                    .cnt(each.getCnt())
                    .ratio(actualRatio)
                    .build();
            rs.add(shortLinkLocalStatsRespDTO);
        });
        return rs;
    }

    private List<ShortLinkDailyStatsRespDTO> buildDailyStats(List<LinkStatsTodayDO> linkStatsTodayDOList, ShortLinkStatsReqDTO param) {
        Map<String, LinkStatsTodayDO> statsMap = linkStatsTodayDOList.stream().collect(Collectors.toMap(
                item -> DateUtil.format(item.getDate(), DatePattern.NORM_DATE_PATTERN),
                Function.identity()
        ));
        List<String> allDates = DateUtil.rangeToList(
                        DateUtil.parse(param.getStartDate().substring(0, 19)),
                        DateUtil.parse(param.getEndDate().substring(0, 19)),
                        DateField.DAY_OF_MONTH)
                .stream()
                .map(DateUtil::formatDate)
                .toList();
        LinkStatsTodayDO defaultStats  = LinkStatsTodayDO.builder()
                .todayPv(0)
                .todayUip(0)
                .todayUv(0)
                .build();
        return allDates.stream()
                .map(date -> {
                    LinkStatsTodayDO stats = statsMap.getOrDefault(date, defaultStats);
                    return ShortLinkDailyStatsRespDTO.builder()
                            .date(date)
                            .uv(stats.getTodayUv())
                            .pv(stats.getTodayPv())
                            .uip(stats.getTodayUip())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
