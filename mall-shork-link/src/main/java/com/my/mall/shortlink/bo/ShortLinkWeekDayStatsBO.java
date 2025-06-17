package com.my.mall.shortlink.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/29
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkWeekDayStatsBO {
    private Integer weekday;
    private Integer count;
}
