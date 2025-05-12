package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.shortlink.entity.LinkDO;
import org.apache.calcite.adapter.java.Map;
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
     * @param gid
     * @return
     */
    IPage<LinkDO> pageRecycle(@Param("gid") List<String> gid);
}
