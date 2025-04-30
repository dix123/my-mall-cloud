package com.my.mall.api.auth.feign;

import com.my.mall.common.core.api.CommonResult;
import com.my.mall.security.bo.UserInfoInTokenBO;
import com.my.mall.security.constant.AuthConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Base
 * @Date: 2025/4/30
 **/
@FeignClient(value = "mall-auth", contextId = "token")
public interface TokenFeignClient {

    /**
     * 校验token
     * @param accessToken
     * @return
     */
    @GetMapping(AuthConstant.CHECK_TOKEN_URI)
    CommonResult<UserInfoInTokenBO> checkToken(@RequestParam("accessToken") String accessToken);

}
