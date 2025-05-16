package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.api.shortlink.dto.GroupShortLinkCountDTO;
import com.my.mall.shortlink.entity.LinkDO;
import feign.Param;
import org.apache.calcite.adapter.java.Map;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/7
 **/
@Mapper
public interface ShortLinkMapper extends BaseMapper<LinkDO> {
    /**
     * gid列表对应的连接数
     *
     * @return
     */
    List<GroupShortLinkCountDTO> listGroupShortLinkCount(@Param("gids") List<String> gisd);
}
