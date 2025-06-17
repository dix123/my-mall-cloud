package com.my.mall.api.auth.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: haole
 * @Date: 2025/4/28
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoInTokenBO {
    private Long id;
    private String username;
}
