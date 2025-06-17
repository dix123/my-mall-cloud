package com.my.mall.api.auth.constant;

import com.my.mall.common.core.feign.FeignInsideAuthConfig;

/**
 * @Author: haole
 * @Date: 2025/4/30
 **/
public interface AuthConstant {
    String AUTH_HEADER = "Token";
    String CHECK_TOKEN_URI = FeignInsideAuthConfig.FEIGN_INSIDE_PREFIX + "/token/checkToken";

    String CHECK_RBAC_URI = FeignInsideAuthConfig.FEIGN_INSIDE_PREFIX + "/insider/permission/checkPermission";
}
