package com.my.mall.shortlink.admin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.my.mall.common.data.model.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: haole
 * @Date: 2025/5/9
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_user")
public class UserDO extends BaseDO {
    private Integer id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String mail;
    private Long deletionTime;
}
