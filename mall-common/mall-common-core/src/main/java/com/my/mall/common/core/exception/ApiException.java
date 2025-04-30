package com.my.mall.common.core.exception;

import com.my.mall.common.core.api.ErrorCode;
import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/4/22
 **/
@Data
public class ApiException extends RuntimeException{
    ErrorCode errorCode;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }
    public ApiException(String msg, ErrorCode errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public ApiException(String msg) {
        super(msg);
    }
    public ApiException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }
}
