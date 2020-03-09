package com.zhongyp.spring.cloud.ribbon.client.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhongyp.
 * @date 2019/12/24
 */
@Configuration
@Slf4j
public class RibbonConfig {

    private static AtomicInteger count = new AtomicInteger();

    @Bean
    @LoadBalanced

    public RestTemplate restTemplate() {
        log.debug("又创建了一个:{}", count);
        return new RestTemplate();
    }
}
