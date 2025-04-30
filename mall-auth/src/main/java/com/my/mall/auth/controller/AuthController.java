package com.my.mall.auth.controller;

import com.my.mall.auth.manager.TokenStore;
import com.my.mall.security.bo.TokenInfoBO;
import com.my.mall.security.bo.UserInfoInTokenBO;
import com.my.mall.auth.dto.AuthenticationDTO;
import com.my.mall.auth.service.UserService;
import com.my.mall.common.core.api.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: haole
 * @Date: 2025/4/27
 **/
@RestController
@Tag(name = "AuthController", description = "统一认证接口")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserService userService;
    @Autowired
    TokenStore tokenStore;

    @PostMapping("/login")
    public CommonResult<TokenInfoBO> login(@Valid @RequestBody AuthenticationDTO authenticationDTO) {
        UserInfoInTokenBO userInfoInTokenBO = userService.getUserInfoInTokenByUserNameAndPassword(authenticationDTO.getUsername(), authenticationDTO.getPassword());

        return CommonResult.success(tokenStore.storeAndGet(userInfoInTokenBO));

    }

}
