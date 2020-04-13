package com.zhongyp.rabbitmq.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/3/20
 * @since jdk1.8
 */
@Configuration
@ConfigurationProperties(prefix = "test.abc")
public class ServerConfig {

    private AtomicInteger sequence = new AtomicInteger(0);
    private String instanceId;

    public String getInstanceId() {
        return instanceId;
    }

    public synchronized void setInstanceId(String instanceId) {
        if (instanceId == null || instanceId.trim().length() == 0) {
            instanceId = "fpay-pay-" + sequence.getAndIncrement();
        }
        this.instanceId = instanceId;
    }
}
