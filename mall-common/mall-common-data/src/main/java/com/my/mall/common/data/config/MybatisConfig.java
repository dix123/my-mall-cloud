package com.my.mall.common.data.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @Author: haole
 * @Date: 2025/4/24
 **/
@Configuration
@EnableTransactionManagement
@MapperScan({"com.my.mall.mapper", "com.my.mall.dao"})
public class MybatisConfig {
}
