package com.my.mall.common.core.constant;

/**
 * 用户状态
 * @Author: haole
 * @Date: 2025/4/27
 **/
public enum UserStatusConstant {
    /**
     * 禁止
     */
    ENABLE(0),
    /**
     * 开启
     */
    DISABLE(1),
    /**
     * 删除
     */
    DELETE(-1)

    ;
    private final Integer status;
    UserStatusConstant(Integer status) {
        this.status = status;
    }
}
