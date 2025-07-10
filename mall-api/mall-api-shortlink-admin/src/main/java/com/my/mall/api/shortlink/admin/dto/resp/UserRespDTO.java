package com.my.mall.api.shortlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.my.mall.common.core.serialize.PhoneDesensitizationSerializer;
import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/5/9
 **/
@Data
public class UserRespDTO {
    private Integer id;
    private String username;
    private String realName;
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;
    private String mail;
}
