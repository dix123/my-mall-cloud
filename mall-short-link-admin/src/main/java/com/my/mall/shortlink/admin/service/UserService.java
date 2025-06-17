package com.my.mall.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.mall.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.my.mall.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.my.mall.shortlink.admin.dto.resp.UserRespDTO;
import com.my.mall.shortlink.admin.entity.UserDO;

/**
 * @Author: Base
 * @Date: 2025/5/9
 **/
public interface UserService extends IService<UserDO> {

    /**
     * 注册账户
     * @param userRegisterReqDTO
     */
    void register(UserRegisterReqDTO userRegisterReqDTO);

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 更新用户
     * @param reqDTO
     */
    void UpdateUser(UserUpdateReqDTO reqDTO);

    /**
     * 检查是否登录
     * @return
     */
    Boolean checkLogin();

    /**
     * 登出
     */
    void logout();

    /**
     * 是否有这个用户
     * @param username
     * @return
     */
    Boolean hasUsername(String username);
}
