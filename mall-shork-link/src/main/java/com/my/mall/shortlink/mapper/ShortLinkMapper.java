package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.shortlink.entity.LinkDO;
import feign.Param;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/7
 **/
public interface ShortLinkMapper extends BaseMapper<LinkDO> {
    /**
     * gid列表对应的连接数
     *
     * @return
     */
    List<GroupShortLinkCountDTO> listGroupShortLinkCount(@Param("gids") List<String> gisd);
}
