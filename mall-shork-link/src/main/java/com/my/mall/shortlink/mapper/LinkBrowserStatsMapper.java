package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.shortlink.bo.ShortLinkBrowserStatsBO;

import com.my.mall.shortlink.entity.LinkBrowserStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Base
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkBrowserStatsMapper extends BaseMapper<LinkBrowserStatsDO> {
    /**
     * 浏览器记录
     * @param linkBrowserStatsDO
     */
    @Insert("INSERT INTO " +
            "t_link_browser_stats (full_short_url, date, cnt, browser, create_time, update_time, del_flag) " +
            "VALUES( #{linkBrowserStats.fullShortUrl}, #{linkBrowserStats.date}, #{linkBrowserStats.cnt}, #{linkBrowserStats.browser}, NOW(), NOW(), 0) " +
            "ON DUPLICATE KEY UPDATE cnt = cnt +  #{linkBrowserStats.cnt};")
    void shortLinkBrowserState(@Param("linkBrowserStats") LinkBrowserStatsDO linkBrowserStatsDO);

    /**
     * 获取浏览器统计信息通过短链接
     * @param param
     * @return
     */
    List<ShortLinkBrowserStatsBO> listBrowserStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 获取浏览器统计信息通过短链接
     * @param param
     * @return
     */
    List<ShortLinkBrowserStatsBO> listBrowserStatsByGroup(ShortLinkStatsReqDTO param);
}
