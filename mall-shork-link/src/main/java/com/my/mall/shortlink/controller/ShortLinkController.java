package com.my.mall.shortlink.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.shortlink.dto.req.ShortLinkCreateReqDTO;
import com.my.mall.shortlink.dto.resp.ShortLinkCreateRespDTO;
import com.my.mall.shortlink.service.ShortLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haole
 * @Date: 2025/5/14
 **/
@RestController
public class ShortLinkController {

    @Autowired
    private ShortLinkService shortLinkService;

    @PostMapping("/api/short-link/v1/create")
    @SentinelResource(value = "create_short_link")
    public CommonResult<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO param) {
        ShortLinkCreateRespDTO shortLink = shortLinkService.createShortLink(param);
        return CommonResult.success(shortLink);
    }


}
