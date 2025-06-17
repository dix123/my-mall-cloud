package com.my.mall.api.shortlink.dto.resp.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/28
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShortLinkDeviceStatsRespDTO {
    private String device;
    private Integer cnt;
    private Double ratio;
}
