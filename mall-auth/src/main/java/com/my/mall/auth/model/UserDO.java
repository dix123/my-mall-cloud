package com.my.mall.auth.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.my.mall.common.data.model.BaseDO;
import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/4/28
 **/
@Data
@TableName("t_user")
public class UserDO extends BaseDO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间戳
     */
    private Long deletionTime;
}
