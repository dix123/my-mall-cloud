package com.my.mall.shortlink.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.api.shortlink.dto.req.ShortLinkStatsReqDTO;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.api.shortlink.dto.req.ShortLinkAccessRecordReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkGroupAccessRecordReqDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkAccessRecordRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkStatsRespDTO;
import com.my.mall.shortlink.service.ShortLinkStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/
@RestController
@RequiredArgsConstructor
public class ShortLinkStatsController {

    private final ShortLinkStatsService shortLinkStatsService;

    @GetMapping("/feign/insider/api/short-link/v1/stats")
    public CommonResult<ShortLinkStatsRespDTO> getShortLinkStatsByShortLink(ShortLinkStatsReqDTO param) {
        ShortLinkStatsRespDTO rs = shortLinkStatsService.getShortLinkStats(param);
        return CommonResult.success(rs);
    }

    @GetMapping("/feign/insider/api/short-link/v1/stats/group")
    public CommonResult<ShortLinkStatsRespDTO> getShortLinkStatsByGroup(ShortLinkStatsReqDTO param) {
        return CommonResult.success(shortLinkStatsService.getShortLinkStatsByGroup(param)   );
    }

    @GetMapping("/feign/insider/api/short-link/v1/stats/access-record")
    public CommonResult<IPage<ShortLinkAccessRecordRespDTO>> getAccessRecord(ShortLinkAccessRecordReqDTO param) {
        return CommonResult.success(shortLinkStatsService.getShortLinkAccessRecordRespDTO(param));
    }

    @GetMapping("/feign/insider/api/short-link/v1/stats/access-record/group")
    public CommonResult<IPage<ShortLinkAccessRecordRespDTO>> getGroupAccessRecord(ShortLinkGroupAccessRecordReqDTO param) {
        return CommonResult.success(shortLinkStatsService.getGroupShortLinkAccessRecord(param));
    }
}
