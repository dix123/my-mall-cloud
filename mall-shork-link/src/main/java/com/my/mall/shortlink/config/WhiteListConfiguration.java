package com.my.mall.shortlink.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/3
 **/
@Data
@Component
@ConfigurationProperties(prefix = "short-link.domain.white-list")
public class WhiteListConfiguration {
    private Boolean enable;
    private List<String> domain;
}
