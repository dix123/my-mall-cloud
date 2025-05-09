package com.my.mall.shortlink.admin.constant;

/**
 * @Author: Base
 * @Date: 2025/5/6
 **/
public interface RedisCacheConstant {
    String CREATE_GROUP_LOCK = "mall:shortLink:createGroup:%s";
    /**
     * 用户注册锁
     */
    String USER_REGISTER_LOCK = "mall:short-link:user:register:";
}
