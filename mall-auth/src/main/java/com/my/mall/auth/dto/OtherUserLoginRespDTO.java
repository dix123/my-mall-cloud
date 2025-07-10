package com.my.mall.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/19
 **/
@Data
@Builder
@AllArgsConstructor
public class OtherUserLoginRespDTO {
    private String token;
    private String userName;
}
