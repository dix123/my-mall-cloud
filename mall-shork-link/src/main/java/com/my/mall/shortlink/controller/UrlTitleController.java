package com.my.mall.shortlink.controller;

import com.my.mall.common.core.api.CommonResult;
import com.my.mall.shortlink.service.UrlTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/
@RestController
@RequiredArgsConstructor
public class UrlTitleController {

    private final UrlTitleService urlTitleService;

    @GetMapping("/feign/insider/api/short-link/v1/title")
    public CommonResult<String> getUrlTitle(@RequestParam("url") String url) {
        return CommonResult.success(urlTitleService.getTitle(url));
    }

}
