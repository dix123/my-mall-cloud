package com.my.mall.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/5/8
 **/
@Data
public class GroupRespDTO {
    private String gid;
    private String name;
    private Integer sortOrder;
    private Integer shortLinkCount;
}
