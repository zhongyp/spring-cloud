package com.zhongyp.rabbitmq.service.impl;

import com.rabbitmq.client.Channel;
import com.zhongyp.rabbitmq.service.config.RabbitmqConfig;
import com.zhongyp.rabbitmq.service.config.ServerConfig;
import com.zhongyp.rabbitmq.service.config.SysConfig;
import lombok.SneakyThrows;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author zhongyp.
 * @date 2020/3/10
 */
@Component
public class Producer {

    @Autowired
    @Qualifier("rabbitTemplate")
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Receiver receiver;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SysConfig sysConfig;

    private String key = "abc";

    @Autowired
    ServerConfig serverConfig;

    @SneakyThrows
    public void producer(String args) {
        int port = sysConfig.getServerPort();
        long count = getMessageCount(RabbitmqConfig.QUEUE_NAME);
//        if (count > 0) {
//            System.out.println(port + "服务获取队列当前不为空，数量为：" + count);
//            return;
//        }
//        boolean flag = getDistributeLockWithExpire(key, 30000);
//        if (!flag) {
//            System.out.println(port + "未获取到锁！");
//        }
//
//        count = getMessageCount(RabbitmqConfig.QUEUE_NAME);
//        if (count > 0) {
//            redisTemplate.delete(key);
//            System.out.println(port + "服务特殊释放锁");
//            return;
//        }
        for (int i = 0; i < 10000; i++) {
            rabbitTemplate.convertAndSend(RabbitmqConfig.FANOUT_EXCHANGE_NAME, "", port + "-message-" + i);
            rabbitTemplate.convertAndSend("abc");
        }
//        if (flag) {
//            redisTemplate.delete(key);
//            System.out.println(port + "服务正常释放锁");
//        }
    }

    @SneakyThrows
    public long getMessageCount(String queueName) {
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Channel channel = null;
        long count = 0;
        try {
            connectionFactory = rabbitTemplate.getConnectionFactory();
            connection = connectionFactory.createConnection();
            channel = connection.createChannel(false);
            count = channel.messageCount(queueName);
        } finally {
            if (connection != null) {
                connection.close();
            }
            if (channel != null) {
                channel.close();
            }
        }
        return count;
    }


    public void sendMessageInNoExistExchange() {
        rabbitTemplate.convertAndSend("test.no", "aaa", "aaaa");
    }

    public boolean getDistributeLockWithExpire(String key, long expire) {
        String redisValue = serverConfig.getInstanceId();
        return (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            boolean flag = false;
            // 设置分布式锁时不要使用setNx,该方法获取锁与设置过期时间不再一个原子操作中，建议使用如下方式
            Object object = connection.execute("set", key.getBytes(), redisValue.getBytes(), "px".getBytes(), String.valueOf(expire).getBytes(), "nx".getBytes());

            if (object == null) {
                return false;
            }
            boolean acquire = String.valueOf(object).equals("OK");

            if (acquire) {
                flag = true;
            } else {
                flag = false;
            }
            return flag;
        });
    }


}
