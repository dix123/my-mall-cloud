package com.my.mall.shortlink.listener;

import cn.hutool.core.annotation.Link;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.my.mall.common.rocketmq.config.RocketMqConstant;
import com.my.mall.shortlink.dto.req.ShortLinkStatsRecordDTO;
import com.my.mall.shortlink.entity.*;
import com.my.mall.shortlink.mapper.*;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * @Author: haole
 * @Date: 2025/5/20
 **/
@Component
@RocketMQMessageListener(topic = RocketMqConstant.SHORT_LINK_STATUS_TOPIC, consumerGroup = RocketMqConstant.SHORT_LINK_STATUS_TOPIC)
public class ShortLinkRecordConsumer implements RocketMQListener<ShortLinkStatsRecordDTO> {

    @Autowired
    private ShortLinkGotoMapper shortLinkGotoMapper;
    @Autowired
    private LinkAccessStatsMapper linkAccessStatsMapper;
    @Autowired
    private LinkLocalStatsMapper linkLocalStatsMapper;
    @Autowired
    private LinkAccessLogsMapper linkAccessLogsMapper;
    @Autowired
    private LinkBrowserStatsMapper linkBrowserStatsMapper;
    @Autowired
    private LinkDeviceStatsMapper linkDeviceStatsMapper;
    @Autowired
    private LinkNetworkStatsMapper linkNetworkStatsMapper;
    @Autowired
    private LinkOsStatsMapper linkOsStatsMapper;
    @Autowired
    private LinkStatsTodayMapper linkStatsTodayMapper;
    @Autowired
    private ShortLinkMapper shortLinkMapper;

    @Override
    public void onMessage(ShortLinkStatsRecordDTO shortLinkStatsRecordDTO) {
        String fullShortUrl = shortLinkStatsRecordDTO.getFullShortUrl();
        LambdaQueryWrapper<ShortLinkGotoDO> wrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                .eq(ShortLinkGotoDO::getFullShortUrl, fullShortUrl);
        ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(wrapper);
        String gid = shortLinkGotoDO.getGid();
        LocalDateTime currentDate = shortLinkStatsRecordDTO.getCurrentDate();
        int hour = currentDate.getHour();
        DayOfWeek week = currentDate.getDayOfWeek();
        int weekValue = week.getValue();
        LinkAccessStatsDO linkAccessStatsDO = LinkAccessStatsDO.builder()
                .fullShortUrl(fullShortUrl)
                .pv(1)
                .uv(shortLinkStatsRecordDTO.getUvFirstFlag() ? 1 : 0)
                .uip(shortLinkStatsRecordDTO.getUipFirstFlag() ? 1 : 0)
                .date(currentDate)
                .hour(hour)
                .weekday(weekValue)
                .build();
        linkAccessStatsMapper.updateRecord(linkAccessStatsDO);
        //更新地区信息
        String province = "未知";
        String city = "未知";
        String adCode = "未知";
        LinkLocaleStatsDO linkLocaleStatsDO = LinkLocaleStatsDO.builder()
                .fullShortUrl(fullShortUrl)
                .date(currentDate)
                .province(province)
                .city(city)
                .adcode(adCode)
                .cnt(1)
                .country("中国")
                .build();
        linkLocalStatsMapper.shortLinkLocaleState(linkLocaleStatsDO);
        LinkOsStatsDO linkOsStatsDO = LinkOsStatsDO.builder()
                .os(shortLinkStatsRecordDTO.getOs())
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(currentDate)
                .build();
        linkOsStatsMapper.shortLinkOsState(linkOsStatsDO);
        LinkBrowserStatsDO linkBrowserStatsDO = LinkBrowserStatsDO.builder()
                .browser(shortLinkStatsRecordDTO.getBrowser())
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(currentDate)
                .build();
        linkBrowserStatsMapper.shortLinkBrowserState(linkBrowserStatsDO);
        LinkDeviceStatsDO linkDeviceStatsDO = LinkDeviceStatsDO.builder()
                .device(shortLinkStatsRecordDTO.getDevice())
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(currentDate)
                .build();
        linkDeviceStatsMapper.shortLinkDeviceState(linkDeviceStatsDO);
        LinkNetworkStatsDO linkNetworkStatsDO = LinkNetworkStatsDO.builder()
                .network(shortLinkStatsRecordDTO.getNetwork())
                .cnt(1)
                .fullShortUrl(fullShortUrl)
                .date(currentDate)
                .build();
        linkNetworkStatsMapper.shortLinkNetworkState(linkNetworkStatsDO);
        LinkAccessLogsDO linkAccessLogsDO = LinkAccessLogsDO.builder()
                .user(shortLinkStatsRecordDTO.getUv())
                .ip(shortLinkStatsRecordDTO.getIp())
                .browser(shortLinkStatsRecordDTO.getBrowser())
                .os(shortLinkStatsRecordDTO.getOs())
                .network(shortLinkStatsRecordDTO.getNetwork())
                .device(shortLinkStatsRecordDTO.getDevice())
                .locale(StrUtil.join("-", "中国", province, city))
                .fullShortUrl(fullShortUrl)
                .build();
        linkAccessLogsMapper.insert(linkAccessLogsDO);
        shortLinkMapper.incrementStats(gid, fullShortUrl, 1, shortLinkStatsRecordDTO.getUvFirstFlag() ? 1 : 0, shortLinkStatsRecordDTO.getUipFirstFlag() ? 1 : 0);
        LinkStatsTodayDO linkStatsTodayDO = LinkStatsTodayDO.builder()
                .todayPv(1)
                .todayUv(shortLinkStatsRecordDTO.getUvFirstFlag() ? 1 : 0)
                .todayUip(shortLinkStatsRecordDTO.getUipFirstFlag() ? 1 : 0)
                .fullShortUrl(fullShortUrl)
                .date(currentDate)
                .build();
        linkStatsTodayMapper.shortLinkTodayState(linkStatsTodayDO);
    }

}
