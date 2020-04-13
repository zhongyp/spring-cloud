package com.zhongyp.rabbitmq.restcontroller;

import com.zhongyp.rabbitmq.service.config.RabbitmqConfig;
import com.zhongyp.rabbitmq.service.impl.Consumer;
import com.zhongyp.rabbitmq.service.impl.Producer;
import com.zhongyp.rabbitmq.service.impl.ReceiveMessage;
import com.zhongyp.rabbitmq.service.impl.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author zhongyp.
 * @date 2020/3/10
 */
@RestController
@RequestMapping("/test")
public class TestRestController {


    @Autowired
    private Producer producer;

    @Autowired
    private Consumer consumer;

    @Autowired
    private SendMessage sendMessage;

    @Autowired
    private ReceiveMessage receiveMessage;


    @RequestMapping("/producer")
    public Map producer() {
        producer.producer("fafafaafafafafafa");
        return null;
    }

    @RequestMapping("/consumer")
    public Map consumer() {
        consumer.consumer();
        return null;
    }

    @RequestMapping("/noexchange")
    public Map channel() {
        producer.sendMessageInNoExistExchange();
        return null;
    }

    @RequestMapping("/nmdl")
    public Map nmdl() {
        sendMessage.sendMessage();
        return null;
    }

    @RequestMapping("/noqueue")
    public Map noqueue() {
        sendMessage.noQueue();
        return null;
    }

    @RequestMapping("/sendconfirm")
    public Map sendMessageWithConfirm() {
        sendMessage.sendMessageWithConfirm("topic", "invoiceRoutingKey", "testnm");
        return null;
    }

    @RequestMapping("/batching")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map batching() {
        sendMessage.batchingSendMessage(RabbitmqConfig.FANOUT_EXCHANGE_NAME, "", "testnmdddddddddddddddddddddddddddddddddd");
        return null;
    }

    @RequestMapping("/transactional")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map testTranscational() {
        sendMessage.testTransactional();
        return null;
    }

    @RequestMapping("/convert")
    public Map convert() {
        sendMessage.convert();
        return null;
    }

    @RequestMapping("/receive")
    public Map receive() {
        receiveMessage.testReceiveMessage();
        return null;
    }

    @RequestMapping("/sendObject")
    public Map sendObject() {
        sendMessage.sendObject();
        return null;
    }
}
