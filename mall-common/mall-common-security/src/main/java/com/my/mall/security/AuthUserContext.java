package com.my.mall.security;

import com.my.mall.api.auth.bo.UserInfoInTokenBO;

/**
 * @Author: haole
 * @Date: 2025/4/29
 **/
public class AuthUserContext {
    private static final ThreadLocal<UserInfoInTokenBO> USER_INFO_IN_TOKEN_HOLDER = new ThreadLocal<>();

    public static UserInfoInTokenBO get() {
        return USER_INFO_IN_TOKEN_HOLDER.get();
    }

    public static void set(UserInfoInTokenBO userInfoInTokenBO) {
        USER_INFO_IN_TOKEN_HOLDER.set(userInfoInTokenBO);
    }

    public static void clean() {
        if (USER_INFO_IN_TOKEN_HOLDER.get() != null) {
            USER_INFO_IN_TOKEN_HOLDER.remove();
        }
    }
}
