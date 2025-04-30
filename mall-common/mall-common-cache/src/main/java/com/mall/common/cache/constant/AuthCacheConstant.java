package com.mall.common.cache.constant;

/**
 * @Author: Base
 * @Date: 2025/4/27
 **/
public interface AuthCacheConstant {

    String AUTH_PREFIX = "project_auth:";
    String AUTH_TOKEN_PREFIX = AUTH_PREFIX + "token:";
    String ACCESS = AUTH_TOKEN_PREFIX + "access:";
    String REFRESH_TO_ACCESS = AUTH_TOKEN_PREFIX + "refresh_to_access:";
    String UID_TO_ACCESS = AUTH_TOKEN_PREFIX + "uid_to_access:";
    /**
     * 1小时过期
     */
    Integer ACCESS_TOKEN_EXPIRE_TIME = 3600;
    /**
     * refreshToken 7天过期
     */
    Integer REFRESH_TOKEN_EXPIRE_TIME = 3600 * 24 * 7;
    /**
     * 进制
     */

    Long SECOND = 1000L;
}
