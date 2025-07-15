package com.my.mall.security.adapter;

import com.my.mall.common.core.feign.FeignInsideAuthConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/4/30
 **/
public class DefaultAuthConfigAdapter implements AuthConfigAdapter{
    /**
     * 内部直接调用接口，无需登录权限
     */
    private static final String FEIGN_INSIDER_URI = FeignInsideAuthConfig.FEIGN_INSIDE_PREFIX + "/insider/**";

    /**
     * 外部直接调用接口，无需登录权限 unwanted auth
     */
    private static final String EXTERNAL_URI = "/**/ua/**";

    /**
     * swagger
     */
    private static final String DOC_URI = "/v3/api-docs";

    /**
     * 注册
     */
    private static final String REGISTER = "/api/short-link/admin/v1/user";
    /**
     * 用户是否存在
     */
    private static final String HAS_USER = "/api/short-link/admin/v1/user/has-username";

    private static final String LOGIN = "/api/short-link/admin/v1/user/login";
    private static final String OTHER_LOGIN = "/api/auth/callback";

    private static final String GOTO = "14.215.41.146";

    @Override
    public String unCheck() {
        return GOTO;
    }

    @Override
    public List<String> pathPatterns() {
        return Collections.singletonList("/*");
    }

    @Override
    public List<String> excludePathPatterns(String... paths) {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(DOC_URI);
        arrayList.add(FEIGN_INSIDER_URI);
        arrayList.add(EXTERNAL_URI);
        arrayList.add(REGISTER);
        arrayList.add(HAS_USER);
        arrayList.add(LOGIN);
        arrayList.add(OTHER_LOGIN);
        arrayList.addAll(Arrays.asList(paths));
        return arrayList;
    }
}
