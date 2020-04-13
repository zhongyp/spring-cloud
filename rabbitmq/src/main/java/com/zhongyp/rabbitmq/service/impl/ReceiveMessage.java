package com.zhongyp.rabbitmq.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.zhongyp.rabbitmq.Bean.TranserDTO;
import com.zhongyp.rabbitmq.service.config.RabbitmqConfig;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/3/26
 * @since jdk1.8
 */
@Service
@EnableRabbit
public class ReceiveMessage {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    ObjectMapper objectMapper = new ObjectMapper();

//    @SneakyThrows
//    @RabbitListener(id = "ack", queues = RabbitmqConfig.QUEUE_NAME)
//    public void testBatchingReceive(@Payload TranserDTO transerDTO, @Header(AmqpHeaders.DELIVERY_TAG) long tag, @Header(AmqpHeaders.CHANNEL) Channel channel) {
////        TranserDTO transerDTO = objectMapper.readValue(new String(message), this.objectMapper.constructType(TranserDTO.class));
//        System.out.println(transerDTO.getA());
//        channel.basicAck(tag, true);
//        channel.close();
//        System.out.println(tag + "确认完成！！！");
//    }

    public void testReceiveMessage() {
        TranserDTO transerDTO = (TranserDTO) rabbitTemplate.receiveAndConvert(RabbitmqConfig.QUEUE_NAME);
        System.out.println(transerDTO.getA());
    }

    @SneakyThrows
    @RabbitListener(id = "ack", queues = RabbitmqConfig.QUEUE_NAME)
    public void testBatchingReceive(@Payload String transerDTO, @Header(AmqpHeaders.DELIVERY_TAG) long tag, @Header(AmqpHeaders.CHANNEL) Channel channel) {
//        TranserDTO transerDTO = objectMapper.readValue(new String(message), this.objectMapper.constructType(TranserDTO.class));
//        System.out.println(transerDTO.getA());
        Thread.sleep(1);
        System.out.println(transerDTO);

        System.out.println(tag + "确认完成！！！");
    }
}
