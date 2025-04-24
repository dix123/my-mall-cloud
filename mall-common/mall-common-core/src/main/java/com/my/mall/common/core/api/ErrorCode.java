package com.my.mall.common.core.api;

/**
 * 错误码
 * @Author: Base
 * @Date: 2025/4/22
 **/
public enum ErrorCode {
    SUCCESS("200", "操作成功"),
    FAIL("500", "操作失败"),
    VALIDATE_FAIL("404", "参数校验失败"),
    UNAUTHORIZED("401", "未登录或token已过期"),
    FORBIDDEN("403", "没有相关权限")
    ;
    private final String code;
    private final String msg;
    ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public String getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}
