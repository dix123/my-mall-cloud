package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.shortlink.bo.ShortLinkOsStatsBO;
import com.my.mall.shortlink.entity.LinkOsStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkOsStatsMapper extends BaseMapper<LinkOsStatsDO> {

    /**
     * 记录地区访问监控数据
     */
    @Insert("INSERT INTO " +
            "t_link_os_stats (full_short_url, date, cnt, os, create_time, update_time, del_flag) " +
            "VALUES( #{linkOsStats.fullShortUrl}, #{linkOsStats.date}, #{linkOsStats.cnt}, #{linkOsStats.os}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkOsStats.cnt};")
    void shortLinkOsState(@Param("linkOsStats") LinkOsStatsDO linkOsStatsDO);

    /**
     * 通过短链接获取操作系统统计信息
     * @param param
     * @return
     */
    List<ShortLinkOsStatsBO> listOsStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 通过短链接获取操作系统统计信息
     * @param param
     * @return
     */
    List<ShortLinkOsStatsBO> listOsStatsByGroup(ShortLinkStatsReqDTO param);
}
