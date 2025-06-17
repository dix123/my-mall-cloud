package com.my.mall.auth.controller;

import com.my.mall.api.auth.dto.UserLoginRespDTO;
import com.my.mall.auth.manager.TokenStore;
import com.my.mall.api.auth.bo.TokenInfoBO;
import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.api.auth.dto.AuthenticationDTO;
import com.my.mall.auth.service.UserService;
import com.my.mall.common.core.api.CommonResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: haole
 * @Date: 2025/4/27
 **/
@RestController
@Tag(name = "AuthController", description = "统一认证接口")
public class AuthController {

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

}
