package com.my.mall.shortlink.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/30
 **/
@RestController
public class ShortLinkNotFoundController {
    /**
     * 短链接不存在跳转页面
     */
    @RequestMapping("/page/notfound")
    public String notfound() {
        return "notfound";
    }
}
