package com.my.mall.shortlink.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: haole
 * @Date: 2025/5/19
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShortLinkStatsRecordDTO {
    private String fullShortUrl;
    private String uv;
    private Boolean uvFirstFlag;
    private Boolean uipFirstFlag;
    private LocalDateTime currentDate;
    private String ip;
    private String os;
    private String device;
    private String browser;
    private String network;

}
