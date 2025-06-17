package com.my.mall.common.core.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/16
 **/
@Configuration
public class ServerConfig {
    @Bean
    public TomcatServletWebServerFactory tomcatCustomizer() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(connector -> {
            // 设置最大请求头大小（单位：字节）
            connector.setProperty("maxHttpHeaderSize", "32768"); // 32KB
        });
        return factory;
    }
}
