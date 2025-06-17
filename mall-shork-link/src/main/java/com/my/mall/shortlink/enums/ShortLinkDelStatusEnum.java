package com.my.mall.shortlink.enums;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/5/26
 **/
public enum ShortLinkDelStatusEnum {
    /**\
     * 未删除
     */
    UNDELETE(0, "未删除"),
    DELETE(1, "删除"),
    MOVE(2, "迁移中")

    ;

    private final Integer status;
    private final String message;

    ShortLinkDelStatusEnum (Integer status, String message) {
        this.status = status;
        this.message = message;
    }
    public Integer value() {
        return status;
    }
    public static ShortLinkDelStatusEnum instance(Integer value) {
        ShortLinkDelStatusEnum[] enums = values();
        for (ShortLinkDelStatusEnum anEnum : enums) {
            if (anEnum.status.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
