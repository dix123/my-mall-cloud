package com.my.mall.api.shortlink.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.common.data.entity.LinkDO;
import lombok.Data;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/27
 **/
@Data
public class ShortLinkPagesReqDto extends Page<LinkDO> {
    private String gid;
    private String orderTag;
}
