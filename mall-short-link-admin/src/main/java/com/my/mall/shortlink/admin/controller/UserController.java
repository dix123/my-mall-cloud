package com.my.mall.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.my.mall.api.auth.dto.AuthenticationDTO;
import com.my.mall.api.auth.dto.UserLoginRespDTO;
import com.my.mall.api.auth.feign.UserFeignClient;
import com.my.mall.api.shortlink.admin.dto.resp.UserRespDTO;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.my.mall.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.resp.UserActualRespDTO;

import com.my.mall.shortlink.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: haole
 * @Date: 2025/5/9
 **/
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFeignClient userFeignClient;

    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public CommonResult<UserRespDTO> getUserByUserName(@PathVariable("username") String username) {
        UserRespDTO userByUsername = userService.getUserByUsername(username);
        return CommonResult.success(userByUsername);
    }

    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public CommonResult<UserActualRespDTO> getActualUserByUserName(@PathVariable("username") String username) {
        UserRespDTO userByUsername = userService.getUserByUsername(username);
        return CommonResult.success(BeanUtil.toBean(userByUsername, UserActualRespDTO.class));
    }

    @PostMapping("/api/short-link/admin/v1/user")
    public CommonResult<Void> register(@RequestBody UserRegisterReqDTO reqDTO) {
        userService.register(reqDTO);
        return CommonResult.success();
    }

    /**
     * 用户登录
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public CommonResult<UserLoginRespDTO> login(@RequestBody AuthenticationDTO requestParam) {
        CommonResult<UserLoginRespDTO> commonResult = userFeignClient.login(requestParam);
        if (!commonResult.isSuccess()) {
            throw new ApiException(commonResult.getMsg());
        }
        return commonResult;
    }

    /**
     * 查询用户名是否存在
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public CommonResult<Boolean> hasUsername(@RequestParam("username") String username) {
        return CommonResult.success(!userService.hasUsername(username));
    }

    @PutMapping("/api/short-link/admin/v1/user")
    public CommonResult<Void> userUpdate(@RequestBody UserUpdateReqDTO userUpdateReqDTO) {
        userService.UpdateUser(userUpdateReqDTO);
        return CommonResult.success();
    }

    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public CommonResult<Boolean> checkLogin() {
        return CommonResult.success(userService.checkLogin());
    }

    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    public CommonResult<Void> logout() {
        userService.logout();
        return CommonResult.success();
    }


}
