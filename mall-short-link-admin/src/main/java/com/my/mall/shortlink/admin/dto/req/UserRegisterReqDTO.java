package com.my.mall.shortlink.admin.dto.req;

import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/5/9
 **/
@Data
public class UserRegisterReqDTO {
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String mail;
}
