package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkDeviceStatsRespDTO;
import com.my.mall.shortlink.entity.LinkDeviceStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkDeviceStatsMapper extends BaseMapper<LinkDeviceStatsDO> {

    /**
     * 设备记录
     * @param linkDeviceStatsDO
     */
    @Insert("INSERT INTO " +
            "t_link_device_stats (full_short_url, date, cnt, device, create_time, update_time, del_flag) " +
            "VALUES( #{linkDeviceStats.fullShortUrl}, #{linkDeviceStats.date}, #{linkDeviceStats.cnt}, #{linkDeviceStats.device}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkDeviceStats.cnt};")
    void shortLinkDeviceState(@Param("linkDeviceStats") LinkDeviceStatsDO linkDeviceStatsDO);

    /**
     * 通过短链接获取设备统计信息
     * @param param
     * @return
     */
    List<ShortLinkDeviceStatsRespDTO> listDeviceStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 通过短链接获取设备统计信息
     * @param param
     * @return
     */
    List<ShortLinkDeviceStatsRespDTO> listDeviceStatsByGroup(ShortLinkStatsReqDTO param);
}
