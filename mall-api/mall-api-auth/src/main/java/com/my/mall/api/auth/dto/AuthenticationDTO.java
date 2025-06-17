package com.my.mall.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: haole
 * @Date: 2025/4/27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
