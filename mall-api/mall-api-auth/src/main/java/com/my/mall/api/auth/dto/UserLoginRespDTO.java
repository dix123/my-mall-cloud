package com.my.mall.api.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/9
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginRespDTO {
    private String token;
}
