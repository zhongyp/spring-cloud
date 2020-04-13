package com.zhongyp.rabbitmq.service.impl;

import com.zhongyp.rabbitmq.service.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhongyp.
 * @date 2020/3/10
 */
@Component
public class Consumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void consumer() {
        System.out.println("接收到消息：" + rabbitTemplate.receiveAndConvert(RabbitmqConfig.QUEUE_NAME));

    }
}
