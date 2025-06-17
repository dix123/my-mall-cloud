package com.my.mall.auth.manager;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.my.mall.common.core.constant.RegexpConstant;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.api.auth.bo.TokenInfoBO;
import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.common.core.constant.AuthCacheConstant;
import com.my.mall.security.AuthUserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: haole
 * @Date: 2025/4/28
 **/
@Component
@Slf4j
public class TokenStore {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public  TokenInfoBO storeAndGet(UserInfoInTokenBO userInfoInToken) {
        TokenInfoBO tokenInfo = new TokenInfoBO();
        tokenInfo.setUserInfoInToken(userInfoInToken);
        tokenInfo.setExpireTime(AuthCacheConstant.ACCESS_TOKEN_EXPIRE_TIME);

        String accessToken = IdUtil.simpleUUID();
        String refreshToken = IdUtil.simpleUUID();
        tokenInfo.setAccessToken(encryptToken(accessToken));
        tokenInfo.setRefreshToken(encryptToken(refreshToken));

        String uidToAccessTokenKeyStr = getUidToAccessKey(userInfoInToken);
        String accessTokenKeyStr = getAccessTokenKey(accessToken);
        String refreshTokenKeyStr = getRefreshTokenKey(refreshToken);

        String oldAccessAndRefresh = stringRedisTemplate.opsForValue().get(uidToAccessTokenKeyStr);

        redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                if (oldAccessAndRefresh != null) {
                    String[] array = oldAccessAndRefresh.split(StrUtil.COLON);
                    operations.delete(getAccessTokenKey(array[0]));
                    operations.delete(getRefreshTokenKey(array[1]));
                }
                operations.opsForValue().set(uidToAccessTokenKeyStr, accessToken + StrUtil.COLON + refreshToken, AuthCacheConstant.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MINUTES);
                operations.opsForValue().set(accessTokenKeyStr, userInfoInToken, AuthCacheConstant.ACCESS_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
                operations.opsForValue().set(refreshTokenKeyStr, userInfoInToken, AuthCacheConstant.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
                operations.exec();
                return null;
            }
        });
        return tokenInfo;

    }

    public void logout(Long id) {
        UserInfoInTokenBO userInfoInTokenBO = new UserInfoInTokenBO();
        userInfoInTokenBO.setId(id);
        String uidToAccessKey = getUidToAccessKey(userInfoInTokenBO);
        String accessKeyArray = (String)redisTemplate.opsForValue().get(uidToAccessKey);
        String[] array = accessKeyArray.split(StrUtil.COLON);
        String accessKey = array[0];
        String refreshKey = array[1];
        redisTemplate.delete(getAccessTokenKey(accessKey));
        redisTemplate.delete(getRefreshTokenKey(refreshKey));
        redisTemplate.delete(uidToAccessKey);
    }

    public UserInfoInTokenBO checkToken(String accessToken) {
        accessToken = decryptToken(accessToken);
        UserInfoInTokenBO userInfoInToken = (UserInfoInTokenBO) redisTemplate.opsForValue().get(getAccessTokenKey(accessToken));
        return userInfoInToken;
    }

    private  String getRefreshTokenKey(String refreshToken) {
        return AuthCacheConstant.REFRESH_TO_ACCESS + refreshToken;
    }

    private  String getAccessTokenKey(String accessToken) {
        return AuthCacheConstant.ACCESS + accessToken;
    }

    private  String getUidToAccessKey(UserInfoInTokenBO userInfoInToken) {
        return AuthCacheConstant.UID_TO_ACCESS + userInfoInToken.getId();
    }
    private String encryptToken(String token) {
        return Base64.encode(token + System.currentTimeMillis());
    }
    private String decryptToken(String data) {
        String decryptStr;
        String decryptToken;
        decryptStr = Base64.decodeStr(data);
        decryptToken = decryptStr.substring(0, 32);
        Long createTokenTime = Long.valueOf(decryptStr.substring(32, 45));
        int expireTime = AuthCacheConstant.ACCESS_TOKEN_EXPIRE_TIME;
        if (System.currentTimeMillis() > createTokenTime + expireTime * AuthCacheConstant.SECOND) {
            throw new ApiException("token过期");
        }
        if (!ReUtil.isMatch(RegexpConstant.SIMPLE_CHAR_REGEXP, decryptToken)) {
            throw new ApiException("token错误");
        }
        return decryptToken;
    }
}
