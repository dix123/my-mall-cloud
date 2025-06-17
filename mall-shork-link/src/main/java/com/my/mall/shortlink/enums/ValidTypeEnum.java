package com.my.mall.shortlink.enums;

import lombok.Getter;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/26
 **/
@Getter
public enum ValidTypeEnum {
    /**
     * 永久有效
     */
    PERMANENT(0),
    CUSTOM(1)
    ;
    private final Integer value;
    ValidTypeEnum (Integer value) {
        this.value = value;
    }
}
