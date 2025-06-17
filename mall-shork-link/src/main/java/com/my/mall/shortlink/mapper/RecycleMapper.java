package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.api.shortlink.dto.req.ShortLinkPageReqDTO;
import com.my.mall.common.data.entity.LinkDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/12
 **/
@Mapper
public interface RecycleMapper extends BaseMapper<LinkDO> {
    /**
     * 分页获取回收站短连接
     * @param param
     * @return
     */
    IPage<LinkDO> pageRecycle(@Param("param") ShortLinkPageReqDTO param);
}
