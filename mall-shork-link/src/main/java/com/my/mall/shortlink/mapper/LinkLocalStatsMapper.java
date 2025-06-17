package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.shortlink.bo.ShortLinkLocalStatsBO;
import com.my.mall.shortlink.entity.LinkLocaleStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkLocalStatsMapper extends BaseMapper<LinkLocaleStatsDO> {
    /**
     * 记录地区数据
     * @param linkLocaleStatsDO
     */
    @Insert("INSERT INTO " +
            "t_link_locale_stats (full_short_url, date, cnt, country, province, city, adcode, create_time, update_time, del_flag) " +
            "VALUES( #{linkLocaleStats.fullShortUrl}, #{linkLocaleStats.date}, #{linkLocaleStats.cnt}, #{linkLocaleStats.country}, #{linkLocaleStats.province}, #{linkLocaleStats.city}, #{linkLocaleStats.adcode}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkLocaleStats.cnt};")
    void shortLinkLocaleState(@Param("linkLocaleStats") LinkLocaleStatsDO linkLocaleStatsDO);

    /**
     * 展示短链接的城市访问信息
     * @param param
     * @return
     */
    List<ShortLinkLocalStatsBO> listLocalStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 展示短链接的城市访问信息
     * @param param
     * @return
     */
    List<ShortLinkLocalStatsBO> listLocalStatsByGroup(ShortLinkStatsReqDTO param);
}
