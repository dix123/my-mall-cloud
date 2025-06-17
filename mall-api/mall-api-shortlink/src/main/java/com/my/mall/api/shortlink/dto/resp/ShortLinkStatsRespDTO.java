package com.my.mall.api.shortlink.dto.resp;

import com.my.mall.api.shortlink.dto.resp.resp.*;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkBrowserStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkDeviceStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkIpStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkLocalStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkNetworkStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkOsStatsRespDTO;
import com.my.mall.api.shortlink.dto.resp.resp.ShortLinkUvStatsRespDTO;
import lombok.*;

import java.util.List;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkStatsRespDTO {
    /**
     * 访问量
     */
    private Integer pv;

    /**
     * 独立访客数
     */
    private Integer uv;

    /**
     * 独立IP数
     */
    private Integer uip;

    /**
     * 基础访问详情
     */
    private List<ShortLinkDailyStatsRespDTO> daily;

    /**
     * 地区访问详情（仅国内）
     */
    private List<ShortLinkLocalStatsRespDTO> localeCnStats;

    /**
     * 小时访问详情
     */
    private List<Integer> hourStats;

    /**
     * 高频访问IP详情
     */
    private List<ShortLinkIpStatsRespDTO> topIpStats;

    /**
     * 一周访问详情
     */
    private List<Integer> weekdayStats;

    /**
     * 浏览器访问详情
     */
    private List<ShortLinkBrowserStatsRespDTO> browserStats;

    /**
     * 操作系统访问详情
     */
    private List<ShortLinkOsStatsRespDTO> osStats;

    /**
     * 访客访问类型详情
     */
    private List<ShortLinkUvStatsRespDTO> uvTypeStats;

    /**
     * 访问设备类型详情
     */
    private List<ShortLinkDeviceStatsRespDTO> deviceStats;

    /**
     * 访问网络类型详情
     */
    private List<ShortLinkNetworkStatsRespDTO> networkStats;
}
