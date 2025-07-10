package com.my.mall.shortlink.admin.feign;

import cn.hutool.core.bean.BeanUtil;
import com.my.mall.api.shortlink.admin.dto.resp.UserRespDTO;
import com.my.mall.common.core.api.CommonResult;

import com.my.mall.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.my.mall.shortlink.admin.entity.UserDO;
import com.my.mall.shortlink.admin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/20
 **/
@RestController
@AllArgsConstructor
public class UserFeignController {

    private final UserService userService;

    @GetMapping("/feign/insider/api/short-link-admin/v1/user")
    public CommonResult<UserRespDTO> getUser(@RequestParam("username") String username) {
        UserRespDTO userByUsername = userService.getUserByUsername(username);
        return CommonResult.success(userByUsername);
    }

    @PostMapping("/feign/insider/api/short-link-admin/v1/user")
    public CommonResult<Void> registerRemoteUser(@RequestParam("username") String username) {
        UserRegisterReqDTO userRegisterReqDTO = UserRegisterReqDTO.builder()
                        .username(username)
                .build();
        userService.register(userRegisterReqDTO);
        return CommonResult.success();
    };
}
