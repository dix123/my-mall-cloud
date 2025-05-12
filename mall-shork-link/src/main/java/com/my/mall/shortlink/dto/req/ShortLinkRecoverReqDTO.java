package com.my.mall.shortlink.dto.req;

import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/5/12
 **/
@Data
public class ShortLinkRecoverReqDTO {
    private String gid;
    private String fullShortUrl;
}
