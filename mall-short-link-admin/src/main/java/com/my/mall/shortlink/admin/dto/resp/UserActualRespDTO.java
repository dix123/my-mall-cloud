package com.my.mall.shortlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.my.mall.shortlink.admin.serialize.PhoneDesensitizationSerializer;
import lombok.Data;

/**
 * @Author: haole
 * @Date: 2025/5/9
 **/
@Data
public class UserActualRespDTO {
    private Integer id;
    private String username;
    private String realName;
    private String phone;
    private String mail;
}
