package com.my.mall.shortlink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan("com.my.mall.**.mapper")
public class MallShorkLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallShorkLinkApplication.class, args);
    }

}
