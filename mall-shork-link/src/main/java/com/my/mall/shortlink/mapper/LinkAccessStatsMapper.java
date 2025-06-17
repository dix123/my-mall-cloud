package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.shortlink.bo.ShortLinkHourStatsBO;
import com.my.mall.shortlink.bo.ShortLinkWeekDayStatsBO;
import com.my.mall.shortlink.entity.LinkAccessStatsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 获取时间统计信息
     * @param param
     * @return
     */
    List<ShortLinkHourStatsBO> listHourStats(ShortLinkStatsReqDTO param);

    /**
     * 获取时间统计信息
     * @param param
     * @return
     */
    List<ShortLinkHourStatsBO> listHourStatsByGroup(ShortLinkStatsReqDTO param);

    /**
     * 根据短链接获取星期统计信息
     * @param param
     * @return
     */
    List<ShortLinkWeekDayStatsBO> listWeekDayStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 根据短链接获取星期统计信息
     * @param param
     * @return
     */
    List<ShortLinkWeekDayStatsBO> listWeekDayStatsByGroup(ShortLinkStatsReqDTO param);
}
