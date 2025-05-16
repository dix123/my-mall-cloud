package com.my.mall.shortlink.dto.resp;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/5/14
 **/
@Data
@Builder
public class ShortLinkCreateRespDTO {
    private String gid;
    private String originalUrl;
    private String fullShortUrl;
}
