package com.my.mall.common.rocketmq.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: haole
 * @Date: 2025/5/16
 **/
@Configuration
@Import({RocketMQAutoConfiguration.class})
@RefreshScope
public class RocketMqAdapter {

    @Autowired
    private RocketMQMessageConverter rocketMQMessageConverter;

    @Value("${rocketmq.name-server}")
    private String nameServer;

    public RocketMQTemplate getTopicByName(String topic) {
        RocketMQTemplate template = new RocketMQTemplate();
        DefaultMQProducer producer = new DefaultMQProducer(topic);
        producer.setNamesrvAddr(nameServer);
        producer.setRetryTimesWhenSendFailed(2);
        producer.setSendMsgTimeout((int) RocketMqConstant.PRODUCER_SEND_TIME_OUT);
        template.setProducer(producer);
        template.setMessageConverter(rocketMQMessageConverter.getMessageConverter());
        return template;
    }
}
