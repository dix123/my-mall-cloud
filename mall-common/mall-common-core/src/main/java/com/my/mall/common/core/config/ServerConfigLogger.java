package com.my.mall.common.core.config;

/**
 * @Description TODO
 * @Author 10855
 * @Date 2025/6/16
 **/
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerConfigLogger implements CommandLineRunner {

    @Value("${server.http.max-header-size:8KB}") // 默认值8KB
    private String maxHeaderSize;

    @Override
    public void run(String... args) {
        System.out.println("Server max header size: " + maxHeaderSize);
    }
}
