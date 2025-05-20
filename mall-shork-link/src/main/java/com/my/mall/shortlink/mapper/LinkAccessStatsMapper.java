package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.shortlink.entity.LinkAccessStatsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author: Base
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkAccessStatsMapper extends BaseMapper<LinkAccessStatsDO> {

    /**
     * 更新所有连接访问记录表
     * @param linkAccessStatsDO
     */
    void updateRecord(@Param("linkAccessStats") LinkAccessStatsDO linkAccessStatsDO);

}
