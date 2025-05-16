package com.my.mall.shortlink.util;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.my.mall.shortlink.constant.ShortLinkGotoConstant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author: haole
 * @Date: 2025/5/14
 **/
public class LinkUtil {
    public static Long getCacheTime(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(each -> LocalDateTimeUtil.between(LocalDateTime.now(), dateTime, DateUnit.SECOND.toChronoUnit()))
                .orElse(ShortLinkGotoConstant.DEFAULT_CACHE_TIME);
    }
}
