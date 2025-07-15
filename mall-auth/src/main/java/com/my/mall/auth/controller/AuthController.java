package com.my.mall.auth.controller;

import com.my.mall.api.auth.dto.UserLoginRespDTO;
import com.my.mall.auth.dto.AccessTokenAndUserNameDTO;
import com.my.mall.auth.dto.OtherUserLoginRespDTO;
import com.my.mall.auth.manager.TokenStore;
import com.my.mall.api.auth.bo.TokenInfoBO;
import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.api.auth.dto.AuthenticationDTO;
import com.my.mall.auth.service.UserService;
import com.my.mall.common.core.api.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: haole
 * @Date: 2025/4/27
 **/
@RestController
@Tag(name = "AuthController", description = "统一认证接口")
public class AuthController {


    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    UserService userService;
    @Autowired
    TokenStore tokenStore;

    @PostMapping("/feign/insider/api/auth/v1/user/login")
    public CommonResult<UserLoginRespDTO> login( @RequestBody AuthenticationDTO authenticationDTO) {
        UserInfoInTokenBO userInfoInTokenBO = userService.getUserInfoInTokenByUserNameAndPassword(authenticationDTO.getUsername(), authenticationDTO.getPassword());
        TokenInfoBO tokenInfoBO = tokenStore.storeAndGet(userInfoInTokenBO);
        return CommonResult.success(UserLoginRespDTO.builder().token(tokenInfoBO.getAccessToken()).build());

    }

    @GetMapping("/api/auth/callback")
    public CommonResult<Void> otherLogin(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.info(code);
        AccessTokenAndUserNameDTO accessTokenAndUserNameDTO = userService.otherLogin(code);
        Cookie cookie = new Cookie("token", accessTokenAndUserNameDTO.getAccessToken());
        cookie.setPath("/callback");
        cookie.setHttpOnly(false);
        cookie.setSecure(true);
        cookie.setMaxAge(3600);
        Cookie usernameCookie = new Cookie("username", accessTokenAndUserNameDTO.getUsername());
        usernameCookie.setPath("/callback");
        usernameCookie.setHttpOnly(false);
        usernameCookie.setSecure(true);
        usernameCookie.setMaxAge(3600);
        response.addCookie(cookie);
        response.addCookie(usernameCookie);
        response.sendRedirect("https://14.215.41.145:11550/callback");
        return CommonResult.success();
    }


}
