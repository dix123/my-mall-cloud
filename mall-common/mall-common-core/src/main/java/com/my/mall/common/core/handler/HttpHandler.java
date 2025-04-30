package com.my.mall.common.core.handler;

import cn.hutool.core.util.CharsetUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.exception.ApiException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @Author: haole
 * @Date: 2025/4/29
 **/
@Component
@Slf4j
public class HttpHandler {
    @Autowired
    ObjectMapper objectMapper;

    public <T> void printToWeb(CommonResult<T> result) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            log.error("requestAttributes is null, can not print");
            return;
        }
        HttpServletResponse response = requestAttributes.getResponse();
        if (response == null) {
            log.error("HttpServletResponse is null, can not print");
            return;
        }
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            PrintWriter writer = response.getWriter();
            writer.write(objectMapper.writeValueAsString(result));
        } catch (IOException e) {
            throw new ApiException("获取writer失败", e);
        }
    }
}
