package com.my.mall.common.core.feign;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 传递认证参数
 * @Author: haole
 * @Date: 2025/5/12
 **/
@Component
@ConditionalOnBean(RequestInterceptor.class)
@Slf4j
public class FeignAuthRequestInterceptor implements RequestInterceptor{

    @Autowired
    private FeignInsideAuthConfig feignInsideAuthConfig;

    @Override
    public void apply(RequestTemplate template) {
        template.header(feignInsideAuthConfig.getKey(), feignInsideAuthConfig.getSecret());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String authorization = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(authorization)) {
            template.header("Authorization", authorization);
        }
    }
}
