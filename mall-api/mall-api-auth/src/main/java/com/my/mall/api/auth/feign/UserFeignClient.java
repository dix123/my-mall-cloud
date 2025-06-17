package com.my.mall.api.auth.feign;

import com.my.mall.api.auth.bo.TokenInfoBO;
import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.api.auth.dto.AuthenticationDTO;
import com.my.mall.api.auth.dto.UserLoginRespDTO;
import com.my.mall.common.core.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/9
 **/
@FeignClient(value = "auth-service")
public interface UserFeignClient {

    @PostMapping("/feign/insider/api/auth/v1/user/login")
    CommonResult<UserLoginRespDTO> login(@RequestBody AuthenticationDTO authenticationDTO);
}
