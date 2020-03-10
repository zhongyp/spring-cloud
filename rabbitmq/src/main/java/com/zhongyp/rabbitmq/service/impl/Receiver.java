package com.zhongyp.rabbitmq.service.impl;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author zhongyp.
 * @date 2020/3/10
 */

@Component
public class Receiver {

    private CountDownLatch latch = new CountDownLatch(1);

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

}
