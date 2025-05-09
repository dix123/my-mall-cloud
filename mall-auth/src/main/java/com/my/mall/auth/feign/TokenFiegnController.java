package com.my.mall.auth.feign;

import com.my.mall.api.auth.feign.TokenFeignClient;
import com.my.mall.auth.manager.TokenStore;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.api.ErrorCode;
import com.my.mall.security.bo.UserInfoInTokenBO;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.avatica.proto.Common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: haole
 * @Date: 2025/4/30
 **/
@RestController
@Slf4j
public class TokenFiegnController implements TokenFeignClient {

    @Autowired
    TokenStore tokenStore;

    @Override
    public CommonResult<UserInfoInTokenBO> checkToken(String accessToken) {
        UserInfoInTokenBO userInfoInTokenBO = tokenStore.checkToken(accessToken);
        if (userInfoInTokenBO == null) {
            return CommonResult.failed(ErrorCode.IDEMPOTENT_TOKEN_DELETE_ERROR);
        }
        return CommonResult.success(userInfoInTokenBO);
    }

    @Override
    public CommonResult<Void> logout(Long id) {
        tokenStore.logout(id);
        return CommonResult.success();
    }
}
