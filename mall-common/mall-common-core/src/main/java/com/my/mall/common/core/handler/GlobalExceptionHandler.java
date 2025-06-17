package com.my.mall.common.core.handler;

import com.my.mall.common.core.api.CommonResult;
import com.my.mall.common.core.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: haole
 * @Date: 2025/4/22
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public CommonResult handle(ApiException ex, HttpServletRequest request) {
        if (ex.getCause() != null) {
            log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString(), ex.getCause());
            return CommonResult.failed(ex.getErrorCode(), ex.getMessage());
        }
        log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString());
        return CommonResult.failed(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult handleValidException(MethodArgumentNotValidException e) {
        log.error("属性校验异常:", e);
        BindingResult bindingResult = e.getBindingResult();
        String msg = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            msg = fieldError.getField() + fieldError.getDefaultMessage();
        }
        return CommonResult.validateFailed(msg);
    }

    @ExceptionHandler(BindException.class)
    public CommonResult handleValidException(BindException e) {
        log.error("绑定异常:", e);
        BindingResult bindingResult = e.getBindingResult();
        String msg = null;
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            msg = fieldError.getField() + fieldError.getDefaultMessage();
        }
        return CommonResult.validateFailed(msg);
    }

    @ExceptionHandler(Exception.class)
    public CommonResult handleException(Exception e) {
        log.error("异常:", e);
        return CommonResult.failed(e.getMessage());
    }
}
