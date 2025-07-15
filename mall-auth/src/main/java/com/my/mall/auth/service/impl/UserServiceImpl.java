package com.my.mall.auth.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.mall.api.auth.bo.TokenInfoBO;
import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.api.shortlink.admin.dto.resp.UserRespDTO;
import com.my.mall.api.shortlink.admin.feign.UserFeignClient;
import com.my.mall.auth.dto.AccessTokenAndUserNameDTO;
import com.my.mall.auth.manager.TokenStore;
import com.my.mall.auth.mapper.UserMapper;
import com.my.mall.auth.service.UserService;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.api.ErrorCode;
import com.my.mall.common.core.exception.ApiException;
import com.my.mall.auth.model.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: haole
 * @Date: 2025/4/28
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private static final String USER_NOT_FOUND_SECRET = "USER_NOT_FOUND_SECRET";
    private static String userNotFoundEncodedPassword;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private TokenStore tokenStore;

    @Override
    public UserInfoInTokenBO getUserInfoInTokenByUserNameAndPassword(String userName, String password) {
        if (StrUtil.isBlank(userName) || StrUtil.isBlank(password)) {
            throw new ApiException("用户名或密码不能为空", ErrorCode.CLIENT_ERROR);
        }
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, userName);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            if (userNotFoundEncodedPassword == null) {
                userNotFoundEncodedPassword = BCrypt.hashpw(USER_NOT_FOUND_SECRET);
            }
            BCrypt.checkpw(password, userNotFoundEncodedPassword);
            throw new ApiException("用户名或密码不正确");
        }
        if (!BCrypt.checkpw(password, userDO.getPassword())) {
            throw new ApiException("用户名或密码不正确");
        }
        return BeanUtil.toBean(userDO, UserInfoInTokenBO.class);

    }

    @Override
    public AccessTokenAndUserNameDTO otherLogin(String code) {
        Map<Object, Object> remoteUserInfo = getRemoteUserInfo(code);
        String userName = (String) remoteUserInfo.get("login");
        CommonResult<UserRespDTO> userResult = userFeignClient.getUser(userName);
        UserRespDTO userInfo = null;
        if (!userResult.isSuccess()) {
            userInfo = registerRemoteUser(userName);
        } else {
            userInfo = userResult.getData();
        }
        TokenInfoBO tokenInfoBO = tokenStore.storeAndGet(BeanUtil.toBean(userInfo, UserInfoInTokenBO.class));
        AccessTokenAndUserNameDTO rs = AccessTokenAndUserNameDTO.builder()
                .username(userInfo.getUsername())
                .accessToken(tokenInfoBO.getAccessToken())
                .build();
        return rs;
    }

    private UserRespDTO registerRemoteUser(String username) {
        CommonResult<Void> registerRemoteUserResult = userFeignClient.registerRemoteUser(username);
        if (!registerRemoteUserResult.isSuccess()) {
            throw new ApiException(registerRemoteUserResult.getMsg());
        }
        CommonResult<UserRespDTO> newUserResult = userFeignClient.getUser(username);
        if (!newUserResult.isSuccess()) {
            throw new ApiException(newUserResult.getMsg());
        }
        return newUserResult.getData();
    }

    private Map<Object, Object> getRemoteUserInfo(String code) {
        Map<String, String> map = new HashMap<>();
        map.put("client_id", "Ov23lixnp1q7Q7Rj3ryY");
        map.put("client_secret", "6b7ec5286d1b14e1557915afa25f0d778611ce85");
        map.put("state", "haole");
        map.put("code", code);
        map.put("redirect_uri", "https://14.215.41.145:24692/api/auth/callback");
        disableSslVerification();
        Map<String,String> resp = restTemplate.postForObject("https://github.com/login/oauth/access_token", map, Map.class);
        HttpHeaders httpheaders = new HttpHeaders();
        httpheaders.add("Authorization", "token " + resp.get("access_token"));
        HttpEntity<?> httpEntity = new HttpEntity<>(httpheaders);
        ResponseEntity<Map> exchange = restTemplate.exchange("https://api.github.com/user", HttpMethod.GET, httpEntity, Map.class);
        Map<Object, Object> bodyMap = exchange.getBody();
        return bodyMap;
    }

    private static void disableSslVerification() {
        try {
            // 创建信任所有证书的信任管理器
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            // 安装信任管理器
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // 创建允许所有主机名验证的主机名验证器
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // 安装主机名验证器
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
