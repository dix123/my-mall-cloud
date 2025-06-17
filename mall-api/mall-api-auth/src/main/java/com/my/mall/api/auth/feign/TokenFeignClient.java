package com.my.mall.api.auth.feign;

import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.api.auth.constant.AuthConstant;
import com.my.mall.common.core.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Base
 * @Date: 2025/4/30
 **/
@FeignClient(value = "auth-service", contextId = "token")
public interface TokenFeignClient {

    /**
     * 校验token
     * @param accessToken
     * @return
     */
    @GetMapping(AuthConstant.CHECK_TOKEN_URI)
    CommonResult<UserInfoInTokenBO> checkToken(@RequestParam("accessToken") String accessToken);

    @PostMapping("/feign/insider/token/logout")
    CommonResult<Void> logout(@RequestBody Long id);

}
