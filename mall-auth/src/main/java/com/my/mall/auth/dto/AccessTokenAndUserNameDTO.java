package com.my.mall.auth.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/20
 **/
@Data
@Builder
public class AccessTokenAndUserNameDTO {
    private String username;
    private String accessToken;
}
