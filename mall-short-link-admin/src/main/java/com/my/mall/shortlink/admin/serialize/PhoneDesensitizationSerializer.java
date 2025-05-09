package com.my.mall.shortlink.admin.serialize;

import cn.hutool.core.util.DesensitizedUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @Author: haole
 * @Date: 2025/5/9
 **/
public class PhoneDesensitizationSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String configName, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String phoneDesensitization = DesensitizedUtil.mobilePhone(configName);
        jsonGenerator.writeString(phoneDesensitization);
    }
}
