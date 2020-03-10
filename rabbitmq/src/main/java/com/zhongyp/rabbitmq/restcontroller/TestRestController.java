package com.zhongyp.rabbitmq.restcontroller;

import com.zhongyp.rabbitmq.service.impl.Consumer;
import com.zhongyp.rabbitmq.service.impl.Producer;
import org.springframework.beans.factory.annotation.Autowired;
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
}
