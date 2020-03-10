package com.zhongyp.rabbitmq.service.impl;

import com.zhongyp.rabbitmq.service.config.RabbitMQConfig;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author zhongyp.
 * @date 2020/3/10
 */
@Component
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Receiver receiver;

    @SneakyThrows
    public void producer(String... args) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME, "spring-cloud-routingKey", "spring-cloud-rabbitmq-test-message");
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);

    }
}
