package com.my.mall.common.core.api;

/**
 * 错误码
 * @Author: Base
 * @Date: 2025/4/22
 **/
public enum ErrorCode {
    /**
     * 成功
     */
    SUCCESS("0", "success"),
    SHOW_FAIL("000001", "show_fail_msg"),
    // ========== 一级宏观错误码 客户端错误 ==========
    CLIENT_ERROR("A000001", "用户端错误"),

    // ========== 二级宏观错误码 用户注册错误 ==========
    USER_REGISTER_ERROR("A000100", "用户注册错误"),
    USER_NAME_VERIFY_ERROR("A000110", "用户名校验失败"),
    USER_NAME_EXIST_ERROR("A000111", "用户名已存在"),
    USER_NAME_SENSITIVE_ERROR("A000112", "用户名包含敏感词"),
    USER_NAME_SPECIAL_CHARACTER_ERROR("A000113", "用户名包含特殊字符"),
    PASSWORD_VERIFY_ERROR("A000120", "密码校验失败"),
    PASSWORD_SHORT_ERROR("A000121", "密码长度不够"),
    PHONE_VERIFY_ERROR("A000151", "手机格式校验失败"),

    /**
     * 无效短链接
     */
    NULL_SHORT_URL("A000201", "无效短链接"),

    // ========== 二级宏观错误码 系统请求缺少幂等Token ==========
    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "幂等Token为空"),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "幂等Token已被使用或失效"),
    FEIGN_AUTH_ERROR("A000202", "feign请求认证错误"),
    PERMISSION_DENY("A000301", "没有指定权限"),

    // ========== 二级宏观错误码 系统请求操作频繁 ==========
    FLOW_LIMIT_ERROR("A000300", "当前系统繁忙，请稍后再试"),


    // ========== 一级宏观错误码 系统执行出错 ==========
    SERVICE_ERROR("B000001", "系统执行出错"),
    // ========== 二级宏观错误码 系统执行超时 ==========
    SERVICE_TIMEOUT_ERROR("B000100", "系统执行超时"),

    // ========== 一级宏观错误码 调用第三方服务出错 ==========
    REMOTE_ERROR("C000001", "调用第三方服务出错");
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
