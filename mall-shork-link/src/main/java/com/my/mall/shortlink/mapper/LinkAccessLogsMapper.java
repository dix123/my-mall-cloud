package com.my.mall.shortlink.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.shortlink.bo.ShortLinkSumStatsBO;
import com.my.mall.shortlink.bo.ShortLinkUvTypeBO;
import com.my.mall.shortlink.bo.UserTypeBO;
import com.my.mall.api.shortlink.dto.req.ShortLinkGroupAccessRecordReqDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkIpStatsRespDTO;
import com.my.mall.api.shortlink.dto.req.entity.LinkAccessLogsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @Author: Base
 * @Date: 2025/5/20
 **/
@Mapper
public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogsDO> {
    /**
     * 获取总的pv、uv、uip
     * @param param
     * @return
     */
    ShortLinkSumStatsBO listSumStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 获取总的pv、uv、uip
     * @param param
     * @return
     */
    ShortLinkSumStatsBO listSumStatsByGroup(ShortLinkStatsReqDTO param);

    /**
     * 获取ip统计信息
     * @param param
     * @return
     */
    List<ShortLinkIpStatsRespDTO> listIpStats(ShortLinkStatsReqDTO param);

    /**
     * 获取ip统计信息
     * @param param
     * @return
     */
    List<ShortLinkIpStatsRespDTO> listIpStatsByGroup(ShortLinkStatsReqDTO param);

    /**
     * 通过短链接获取新老用户统计信息
     * @param param
     * @return
     */
    ShortLinkUvTypeBO listUvTypeStatsByShortLink(ShortLinkStatsReqDTO param);

    /**
     * 通过短链接获取新老用户统计信息
     * @param param
     * @return
     */
    ShortLinkUvTypeBO listUvTypeStatsByGroup(ShortLinkStatsReqDTO param);

    /**
     * 获取指定短链接的新老用户情况
     * @param fullShortUrl
     * @param startDate
     * @param endDate
     * @param userSet
     * @return
     */
    List<UserTypeBO> getUserType(@Param("fullShortUrl") String fullShortUrl,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate,
                                 @Param("userSet") Set<String> userSet);

    /**
     * 获取指定短链接的新老用户情况
     * @param gid
     * @param startDate
     * @param endDate
     * @param userSet
     * @return
     */
    List<UserTypeBO> getGroupUserType(@Param("gid") String gid,
                                 @Param("startDate") String startDate,
                                 @Param("endDate") String endDate,
                                 @Param("userSet") Set<String> userSet);

    /**
     * 获取指定gid和时间下的访问记录
     * @param param
     * @return
     */
    IPage<LinkAccessLogsDO> getAccessLogByGroup(ShortLinkGroupAccessRecordReqDTO param);
}
