package com.my.mall.shortlink.service;

import cn.hutool.core.annotation.Link;
import com.baomidou.mybatisplus.extension.service.IService;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.shortlink.entity.LinkDO;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/7
 **/
public interface ShortLinkService extends IService<LinkDO> {

    /**
     * 获取分组下短连接数量
     * @param gids
     * @return
     */
    List<GroupShortLinkCountDTO> listGroupShortLinkCount(List<String> gids);
}
