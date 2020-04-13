package com.zhongyp.cloud.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class CloudBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudBusApplication.class, args);
    }

}
