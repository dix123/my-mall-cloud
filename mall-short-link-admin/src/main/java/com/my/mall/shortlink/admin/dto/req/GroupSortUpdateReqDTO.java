package com.my.mall.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/5/8
 **/
@Data
public class GroupSortUpdateReqDTO {
    private String gid;
    private Integer sortOrder;
}
