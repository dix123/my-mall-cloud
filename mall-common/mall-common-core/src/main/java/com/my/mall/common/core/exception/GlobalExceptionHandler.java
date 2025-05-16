package com.my.mall.common.core.exception;

import com.my.mall.common.core.api.CommonResult;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public CommonResult handle(ApiException e) {
        if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResult handleValidException(MethodArgumentNotValidException e) {
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

        return CommonResult.failed(e.getMessage());
    }
}
