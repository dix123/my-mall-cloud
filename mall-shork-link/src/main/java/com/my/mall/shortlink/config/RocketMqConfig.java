package com.my.mall.shortlink.config;

import com.my.mall.common.rocketmq.config.RocketMqAdapter;
import com.my.mall.common.rocketmq.config.RocketMqConstant;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * @Author: haole
 * @Date: 2025/5/16
 **/
@Configuration
@RefreshScope
public class RocketMqConfig {

    @Autowired
    private RocketMqAdapter rocketMqAdapter;

    @Lazy
    @Bean(destroyMethod = "destroy")
    public RocketMQTemplate shortLinkStatusTemplate() {
        return rocketMqAdapter.getTopicByName(RocketMqConstant.SHORT_LINK_STATUS_TOPIC);
    }



}
