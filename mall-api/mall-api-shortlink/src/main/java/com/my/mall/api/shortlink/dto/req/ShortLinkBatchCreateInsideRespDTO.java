package com.my.mall.api.shortlink.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkBatchCreateInsideRespDTO {
    private String describe;
    private String originUrl;
    private String shortUrl;
}
