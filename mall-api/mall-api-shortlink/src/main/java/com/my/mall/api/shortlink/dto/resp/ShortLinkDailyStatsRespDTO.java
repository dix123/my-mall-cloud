package com.my.mall.api.shortlink.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkDailyStatsRespDTO {
    private String date;
    private Integer pv;
    private Integer uv;
    private Integer uip;
}
