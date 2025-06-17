package com.my.mall.shortlink.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.api.shortlink.dto.req.ShortLinkAccessRecordReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkGroupAccessRecordReqDTO;

import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkAccessRecordRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkStatsRespDTO;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/28
 **/
public interface ShortLinkStatsService {
    /**
     * 查看短链接统计信息
     * @param param
     * @return
     */
    ShortLinkStatsRespDTO getShortLinkStats(ShortLinkStatsReqDTO param);

    /**
     * 查看分组的短链接统计信息
     * @param param
     * @return
     */
    ShortLinkStatsRespDTO getShortLinkStatsByGroup(ShortLinkStatsReqDTO param);

    /**
     * 获取短链接指定时间的访问记录
     * @param param
     * @return
     */
    IPage<ShortLinkAccessRecordRespDTO> getShortLinkAccessRecordRespDTO(ShortLinkAccessRecordReqDTO param);

    /**
     * 获取分组下指定时间的访问记录
     * @param param
     * @return
     */
    IPage<ShortLinkAccessRecordRespDTO> getGroupShortLinkAccessRecord(ShortLinkGroupAccessRecordReqDTO param);
}
