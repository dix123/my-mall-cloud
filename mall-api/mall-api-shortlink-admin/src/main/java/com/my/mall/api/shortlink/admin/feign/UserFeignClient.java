package com.my.mall.api.shortlink.admin.feign;

import com.my.mall.api.shortlink.admin.dto.resp.UserRespDTO;
import com.my.mall.common.core.api.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/20
 **/
@FeignClient(value = "short-link-admin", contextId = "UserInfo")
public interface UserFeignClient {

    @GetMapping("/feign/insider/api/short-link-admin/v1/user")
    CommonResult<UserRespDTO> getUser(@RequestParam("username") String username);

    @PostMapping("/feign/insider/api/short-link-admin/v1/user")
    CommonResult<Void> registerRemoteUser(@RequestParam("username") String username);
}
