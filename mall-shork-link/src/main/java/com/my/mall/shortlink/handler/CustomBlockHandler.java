package com.my.mall.shortlink.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: haole
 * @Date: 2025/5/13
 **/
@Component
public class CustomBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
        // 设置响应格式
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // 构建统一的错误响应
        Map<String, Object> result = new HashMap<>();
        // HTTP 429 Too Many Requests
        result.put("code", 429);

        // 根据不同的异常类型返回不同的错误信息
        if (ex instanceof FlowException) {
            result.put("message", "请求被限流，请稍后再试");
        } else if (ex instanceof DegradeException) {
            result.put("message", "服务暂时不可用，请稍后再试");
        } else if (ex instanceof ParamFlowException) {
            result.put("message", "热点参数请求过多，请稍后再试");
        } else if (ex instanceof SystemBlockException) {
            result.put("message", "系统负载过高，请稍后再试");
        } else if (ex instanceof AuthorityException) {
            result.put("message", "没有访问权限");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else {
            result.put("message", "系统繁忙，请稍后再试");
        }

        // 输出JSON响应
        response.getWriter().write(JSON.toJSONString(result));
    }
}
