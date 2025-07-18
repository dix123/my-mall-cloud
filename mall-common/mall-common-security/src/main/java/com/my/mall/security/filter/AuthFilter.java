package com.my.mall.security.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.my.mall.api.auth.feign.TokenFeignClient;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.api.ErrorCode;
import com.my.mall.common.core.handler.HttpHandler;
import com.my.mall.common.core.util.IpHelper;
import com.my.mall.security.AuthUserContext;
import com.my.mall.security.adapter.AuthConfigAdapter;
import com.my.mall.api.auth.bo.UserInfoInTokenBO;
import com.my.mall.common.core.feign.FeignInsideAuthConfig;
import com.my.mall.api.auth.constant.AuthConstant;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @Author: haole
 * @Date: 2025/4/29
 **/
@Component
@Slf4j
public class AuthFilter implements Filter {

    @Autowired
    private FeignInsideAuthConfig feignInsideAuthConfig;

    @Autowired
    private HttpHandler httpHandler;

    @Autowired
    private AuthConfigAdapter authConfigAdapter;
    @Autowired
    private TokenFeignClient tokenFeignClient;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if (!checkFeignRequest(req)) {
            httpHandler.printToWeb(CommonResult.failed(ErrorCode.FEIGN_AUTH_ERROR));
            return;
        }

        if (AuthConstant.CHECK_TOKEN_URI.equals(req.getRequestURI())) {
            filterChain.doFilter(req, resp);
            return;
        }
        List<String> excludePathPatterns = authConfigAdapter.excludePathPatterns();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        for (String path : excludePathPatterns) {
            if (pathMatcher.match(path, req.getRequestURI())) {
                filterChain.doFilter(req, resp);
                return;
            }
        }

        if (req.getServerName().contains(authConfigAdapter.unCheck())) {
            filterChain.doFilter(req, resp);
            return;
        }

        String accessToken = req.getHeader(AuthConstant.AUTH_HEADER);
        if (StrUtil.isBlank(accessToken)) {
            httpHandler.printToWeb(CommonResult.failed(ErrorCode.IDEMPOTENT_TOKEN_NULL_ERROR));
            return;
        }
        CommonResult<UserInfoInTokenBO> userInfoInTokenBOCommonResult = tokenFeignClient.checkToken(accessToken);
        if (!userInfoInTokenBOCommonResult.isSuccess()) {
            httpHandler.printToWeb(CommonResult.failed(ErrorCode.IDEMPOTENT_TOKEN_DELETE_ERROR));
            return;
        }
        UserInfoInTokenBO userInfoInToken = userInfoInTokenBOCommonResult.getData();
        if (!checkRbac(userInfoInToken, req.getRequestURI(), req.getMethod())) {
            httpHandler.printToWeb(CommonResult.failed(ErrorCode.PERMISSION_DENY));
            return;
        }
        try {
            AuthUserContext.set(userInfoInToken);
            filterChain.doFilter(req, resp);
        } finally {
            AuthUserContext.clean();
        }


    }

    private boolean checkRbac(UserInfoInTokenBO userInfoInToken, String requestURI, String method) {
        return true;
    }

    private boolean checkFeignRequest(HttpServletRequest req) {
        if (!req.getRequestURI().startsWith(FeignInsideAuthConfig.FEIGN_INSIDE_PREFIX)) {
            return true;
        }
        String secret = req.getHeader(feignInsideAuthConfig.getKey());
        if (StrUtil.isBlank(secret) || !Objects.equals(secret, feignInsideAuthConfig.getSecret())) {
            return false;
        }
        List<String> ips = feignInsideAuthConfig.getIps();
        ips.removeIf(StrUtil::isBlank);
        if (CollectionUtil.isEmpty(ips)) {
            return true;
        } else if (!ips.contains(IpHelper.getIpAddr())) {
            log.error("ip is not in white list,{}, ip, {}", ips, IpHelper.getIpAddr());
            return false;
        }
        return true;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
