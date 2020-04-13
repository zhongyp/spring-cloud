package com.zhongyp.rabbitmq.service.impl;

import com.zhongyp.rabbitmq.util.Sequence;
import com.zhongyp.rabbitmq.util.ThreadProcessor;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @author zhongyp.
 * @date 2020/3/10
 */

@Component
public class Receiver {

    @Autowired
    RabbitTemplate rabbitTemplate;

    private CountDownLatch latch = new CountDownLatch(1);

    private Semaphore semaphore = new Semaphore(8);

    @SneakyThrows
    @RabbitListener
    public void receiveMessage(String message) {
        semaphore.acquire();
        ThreadProcessor.submit(new MessageProcessorTask("任务" + Sequence.getSequence(), message));
//        System.out.println("Received <" + message + ">");
//        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    class MessageProcessorTask implements Callable {

        private String taskName;
        private String message;

        public MessageProcessorTask(String taskName, String message) {
            this.message = message;
            this.taskName = taskName;
        }

        @Override
        @SneakyThrows
        public Object call() throws Exception {
//            Thread.sleep(1000);
            System.out.println(this.taskName + "服务收到消息：" + this.message);
            semaphore.release();
            return null;
        }
    }
}
