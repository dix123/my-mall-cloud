package com.my.mall.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.my.mall.api.shortlink.dto.req.ShortLinkAccessRecordReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkGroupAccessRecordReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkAccessRecordRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkStatsRespDTO;
import com.my.mall.api.shortlink.feign.ShortLinkStatsFeignClient;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.api.shortlink.dto.req.ShortLinkGroupStatsReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/
@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {

    private final ShortLinkStatsFeignClient shortLinkStatsFeignClient;

    /**
     * 访问单个短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats")
    public CommonResult<ShortLinkStatsRespDTO> shortLinkStats(ShortLinkStatsReqDTO requestParam) {
        return shortLinkStatsFeignClient.oneShortLinkStats(
                requestParam.getFullShortUrl(),
                requestParam.getGid(),
                requestParam.getEnableStatus(),
                requestParam.getStartDate(),
                requestParam.getEndDate()
        );
    }

    /**
     * 访问分组短链接指定时间内监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/group")
    public CommonResult<ShortLinkStatsRespDTO> groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam) {
        return shortLinkStatsFeignClient.groupShortLinkStats(
                requestParam.getGid(),
                requestParam.getStartDate(),
                requestParam.getEndDate()
        );
    }

    /**
     * 访问单个短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record")
    public CommonResult<Page<ShortLinkAccessRecordRespDTO>> shortLinkStatsAccessRecord(ShortLinkAccessRecordReqDTO requestParam) {
        return shortLinkStatsFeignClient.shortLinkStatsAccessRecord(
                requestParam.getFullShortUrl(),
                requestParam.getGid(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                requestParam.getEnableStatus(),
                requestParam.getCurrent(),
                requestParam.getSize()
        );
    }

    /**
     * 访问分组短链接指定时间内访问记录监控数据
     */
    @GetMapping("/api/short-link/admin/v1/stats/access-record/group")
    public CommonResult<Page<ShortLinkAccessRecordRespDTO>> groupShortLinkStatsAccessRecord(ShortLinkGroupAccessRecordReqDTO requestParam) {
        return shortLinkStatsFeignClient.groupShortLinkStatsAccessRecord(
                requestParam.getGid(),
                requestParam.getStartDate(),
                requestParam.getEndDate(),
                requestParam.getCurrent(),
                requestParam.getSize()
        );
    }
}
