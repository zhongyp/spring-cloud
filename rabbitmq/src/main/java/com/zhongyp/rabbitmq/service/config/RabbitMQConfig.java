package com.zhongyp.rabbitmq.service.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhongyp.
 * @date 2020/3/10
 */
@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "spring-cloud-rabbit-queue";
    public static final String FANOUT_EXCHANGE_NAME = "spring-cloud-rabbit-fanout-exchange";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Exchange exchange() {
        return new FanoutExchange(FANOUT_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding binding(Queue queue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(QUEUE_NAME);
//        container.setMessageListener(listenerAdapter);
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
}
