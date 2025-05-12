package com.my.mall.shortlink.dto.req;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.shortlink.entity.LinkDO;
import lombok.Data;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/5/12
 **/
@Data
public class ShortLinkPageReqDTO extends Page<LinkDO> {
    private List<String> gids;
}
