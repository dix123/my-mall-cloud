package com.my.mall.shortlink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "com.my.mall.**")
@EnableFeignClients(basePackages = "com.my.mall.**.feign")
@MapperScan({ "com.my.mall.**.mapper" })
@EnableAspectJAutoProxy(exposeProxy = true)
public class MallShorkLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallShorkLinkApplication.class, args);
    }

}
