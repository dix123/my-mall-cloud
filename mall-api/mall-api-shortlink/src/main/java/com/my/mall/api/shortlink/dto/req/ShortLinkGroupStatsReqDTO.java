package com.my.mall.api.shortlink.dto.req;

import lombok.Data;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/
@Data
public class ShortLinkGroupStatsReqDTO {
    private String gid;
    private String startDate;
    private String endDate;
}
