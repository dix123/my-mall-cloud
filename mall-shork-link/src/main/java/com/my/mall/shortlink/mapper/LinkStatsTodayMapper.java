package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.shortlink.entity.LinkStatsTodayDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkStatsTodayMapper extends BaseMapper<LinkStatsTodayDO> {

    /**
     * 今日监控数据
     * @param linkStatsTodayDO
     */
    @Insert("INSERT INTO " +
            "t_link_stats_today (full_short_url, date,  today_uv, today_pv, today_uip, create_time, update_time, del_flag) " +
            "VALUES( #{linkTodayStats.fullShortUrl}, #{linkTodayStats.date}, #{linkTodayStats.todayUv}, #{linkTodayStats.todayPv}, #{linkTodayStats.todayUip}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE today_uv = today_uv +  #{linkTodayStats.todayUv}, today_pv = today_pv +  #{linkTodayStats.todayPv}, today_uip = today_uip +  #{linkTodayStats.todayUip};")
    void shortLinkTodayState(@Param("linkTodayStats") LinkStatsTodayDO linkStatsTodayDO);

    /**
     * 获取指定时间内的每日统计信息
     * @param param
     * @return
     */
    List<LinkStatsTodayDO> listStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 获取指定时间内的每日统计信息、分组
     * @param param
     * @return
     */
    List<LinkStatsTodayDO> listStatsByGroup(ShortLinkStatsReqDTO param);
}
