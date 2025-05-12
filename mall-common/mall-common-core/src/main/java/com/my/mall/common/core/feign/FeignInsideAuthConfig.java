package com.my.mall.common.core.feign;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: haole
 * @Date: 2025/4/30
 **/
@RefreshScope
@ConfigurationProperties(prefix = "feign.inside")
@Configuration
@Data
public class FeignInsideAuthConfig {

    public static final String FEIGN_INSIDE_PREFIX = "/feign";

    @Value("${key}")
    private String key;
    @Value("${secret}")
    private String secret;
    @Value("#{'${ips}'.split(',')}")
    private List<String> ips;
}
