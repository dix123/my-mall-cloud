package com.my.mall.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.auth.dto.AccessTokenAndUserNameDTO;
import com.my.mall.auth.model.UserDO;

/**
 * @Author: Base
 * @Date: 2025/4/28
 **/
public interface UserService extends IService<UserDO> {
    /**
     * 通过用户名和密码获取用户信息
     * @param userName
     * @param password
     * @return
     */
    UserInfoInTokenBO getUserInfoInTokenByUserNameAndPassword(String userName, String password);

    /**
     * 第三方登錄邏輯
     * @param code
     * @return
     */
    AccessTokenAndUserNameDTO otherLogin(String code);
}
