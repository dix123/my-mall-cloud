package com.my.mall.common.exception;

import com.my.mall.common.api.ErrorCode;

/**
 * @Author: haole
 * @Date: 2025/4/22
 **/
public class Asserts {
    public static void fail(String msg){
        throw  new ApiException(msg);
    }

    public static void fail(ErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
