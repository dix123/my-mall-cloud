package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkNetworkStatsRespDTO;
import com.my.mall.shortlink.entity.LinkNetworkStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkNetworkStatsMapper extends BaseMapper<LinkNetworkStatsDO> {

    /**
     * 记录网路
     * @param linkNetworkStatsDO
     */
    @Insert("INSERT INTO " +
            "t_link_network_stats (full_short_url, date, cnt, network, create_time, update_time, del_flag) " +
            "VALUES( #{linkNetworkStats.fullShortUrl}, #{linkNetworkStats.date}, #{linkNetworkStats.cnt}, #{linkNetworkStats.network}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkNetworkStats.cnt};")
    void shortLinkNetworkState(@Param("linkNetworkStats") LinkNetworkStatsDO linkNetworkStatsDO);

    /**
     * 根据短链接获取网络统计信息
     * @param param
     * @return
     */
    List<ShortLinkNetworkStatsRespDTO> listNetworkStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 根据短链接获取网络统计信息
     * @param param
     * @return
     */
    List<ShortLinkNetworkStatsRespDTO> listNetworkStatsByGroup(ShortLinkStatsReqDTO param);
}
