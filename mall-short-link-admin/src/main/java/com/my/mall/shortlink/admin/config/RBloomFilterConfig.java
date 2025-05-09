package com.my.mall.shortlink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: haole
 * @Date: 2025/5/6
 **/
@Configuration
public class RBloomFilterConfig {
    /**
     * 防止用户注册查询数据库的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> userRegisterBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("userRegisterBloomFilter");
        cachePenetrationBloomFilter.tryInit(100000000L, 0.001);
        return cachePenetrationBloomFilter;
    }

    /**
     * 防止分组标识注册查询数据库的布隆过滤器
     */
    @Bean
    public RBloomFilter<String> gidRegisterBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter("gidRegisterBloomFilter");
        cachePenetrationBloomFilter.tryInit(200000000L, 0.001);
        return cachePenetrationBloomFilter;
    }
}
