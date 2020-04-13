package com.zhongyp.rabbitmq.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhongyp.rabbitmq.Bean.TranserDTO;
import com.zhongyp.rabbitmq.service.config.RabbitmqConfig;
import lombok.SneakyThrows;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/3/26
 * @since jdk1.8
 */
@Service
public class SendMessage {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    BatchingRabbitTemplate batchingRabbitTemplate;

    public void sendMessage() {
        for (int i = 0; i < 10000; i++) {
            rabbitTemplate.convertAndSend("auto.exch", "invoiceRoutingKey", "testnm");
        }
    }

    public void noQueue() {
        rabbitTemplate.convertAndSend("topic", "invoiceRoutingKey", "testnm");
    }

    @SneakyThrows
    public void sendMessageWithConfirm(String exchangeName, String routingKey, String msg) {
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.convertAndSend(exchangeName, routingKey, msg, correlationData);
        if (!correlationData.getFuture().get().isAck()) {
            throw new RuntimeException(new String(correlationData.getReturnedMessage().getBody()) + "消息发送失败！失败原因如下：" + correlationData.getFuture().get().getReason());
        }
    }

    public void batchingSendMessage(String exchangeName, String routingKey, String msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setInferredArgumentType(String.class);
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
        for (int i = 0; i < 11; i++) {
            if (i == 10) {
                throw new RuntimeException();
            }
            batchingRabbitTemplate.send(RabbitmqConfig.FANOUT_EXCHANGE_NAME, "", MessageBuilder.withBody(msg.getBytes()).copyProperties(messageProperties).build(), null);
        }

    }

    public void testTransactional() {
        for (int i = 0; i < 1000; i++) {
//            if (i == 5) {
//                throw new RuntimeException();
//            }
            rabbitTemplate.convertAndSend(RabbitmqConfig.FANOUT_EXCHANGE_NAME, "", "testnm" + i);
        }
    }

    public void convert() {
        for (int i = 0; i < 10999; i++) {
            rabbitTemplate.send(RabbitmqConfig.FANOUT_EXCHANGE_NAME, "", MessageBuilder.withBody(("abc" + i).getBytes()).setContentType(MessageProperties.CONTENT_TYPE_TEXT_PLAIN).build());
        }
    }

    public void sendObject() {
        for (int i = 0; i < 10; i++) {
            TranserDTO transerDTO = new TranserDTO();
            transerDTO.setA("a" + i);
            transerDTO.setB("b" + i);
            transerDTO.setC("c" + i);
            transerDTO.setD(i);
            List list = new ArrayList<>();
            list.add(i);
            transerDTO.setS(list);
            rabbitTemplate.convertAndSend(RabbitmqConfig.FANOUT_EXCHANGE_NAME, "", transerDTO);

        }
    }

    @SneakyThrows
    public void convertObject() {
        for (int i = 0; i < 10; i++) {
            TranserDTO transerDTO = new TranserDTO();
            transerDTO.setA("a" + i);
            transerDTO.setB("b" + i);
            transerDTO.setC("c" + i);
            transerDTO.setD(i);
            List list = new ArrayList<>();
            list.add(i);
            transerDTO.setS(list);
            rabbitTemplate.send(RabbitmqConfig.FANOUT_EXCHANGE_NAME, "", MessageBuilder.withBody(new ObjectMapper().writeValueAsBytes(transerDTO)).setContentType(MessageProperties.CONTENT_TYPE_JSON).setHeader(DefaultClassMapper.DEFAULT_CLASSID_FIELD_NAME, "transerDTO").build());
        }
    }
}
