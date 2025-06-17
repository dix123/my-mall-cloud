package com.my.mall.shortlink.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.my.mall.api.shortlink.dto.resp.ShortLinkBatchCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkCreateRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkGroupLinkCountQueryRespDTO;
import com.my.mall.api.shortlink.dto.resp.ShortLinkPageRespDTO;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.api.shortlink.dto.req.ShortLinkBatchCreateReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkCreateReqDTO;
import com.my.mall.api.shortlink.dto.req.ShortLinkPagesReqDto;
import com.my.mall.api.shortlink.dto.req.ShortLinkUpdateReqDTO;
import com.my.mall.shortlink.service.ShortLinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/5/14
 **/
@RestController
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;

    @PostMapping("/feign/insider/api/short-link/v1/create")
    @SentinelResource(value = "create_short_link")
    public CommonResult<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO param) {
        ShortLinkCreateRespDTO shortLink = shortLinkService.createShortLink(param);
        return CommonResult.success(shortLink);
    }

    @GetMapping("/{short-uri}")
    public void redirect(@PathVariable("short-uri") String shortUri, HttpServletRequest request, HttpServletResponse response) {
        shortLinkService.redirectUrl(shortUri, request, response);
    }

    @PostMapping("/feign/insider/api/short-link/v1/create/batch")
    public CommonResult<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO param) {
        ShortLinkBatchCreateRespDTO shortLinkBatchCreateRespDTO = shortLinkService.batchCreateShortLink(param);
        return CommonResult.success(shortLinkBatchCreateRespDTO);
    }

    @PostMapping("/feign/insider/api/short-link/v1/update")
    public CommonResult<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO param) {
        shortLinkService.updateShortLink(param);
        return CommonResult.success();
    }

    @GetMapping("/feign/insider/api/short-link/v1/page")
    public CommonResult<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPagesReqDto param) {
        return CommonResult.success(shortLinkService.pageShortLink(param));
    }

    @GetMapping("/api/short-link/v1/count")
    public CommonResult<List<ShortLinkGroupLinkCountQueryRespDTO>> listGroupLinkCount(List<String> gidList) {
        return CommonResult.success(shortLinkService.countShortLink(gidList));
    }
}
