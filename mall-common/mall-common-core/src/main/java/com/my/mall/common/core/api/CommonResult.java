package com.my.mall.common.core.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: haole
 * @Date: 2025/4/22
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult <T>{
    private String code;
    private String msg;
    private T data;

    public static <F> CommonResult<F> success(F data) {
        return new CommonResult<>(ErrorCode.SUCCESS.getCode(), ErrorCode.SUCCESS.getMsg(), data);
    }

    public static <F> CommonResult<F> success(F data, String msg) {
        return new CommonResult<>(ErrorCode.SUCCESS.getCode(), msg, data);
    }

    public static <F> CommonResult<F> failed(ErrorCode errorCode) {
        return new CommonResult<>(errorCode.getCode(), errorCode.getMsg(), null);
    }

    public static <F> CommonResult<F> failed(ErrorCode errorCode, String msg) {
        return new CommonResult<>(errorCode.getCode(), msg, null);
    }

    public static <F> CommonResult<F> failed(String msg) {
        return new CommonResult<>(ErrorCode.SHOW_FAIL.getCode(), msg, null);
    }

    public static <F> CommonResult<F> validateFailed(String msg) {
        return new CommonResult<>(ErrorCode.CLIENT_ERROR.getCode(), msg, null);
    }
}
