package com.zhongyp.spring.cloud.config.client.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhongyp.
 * @date 2019/12/22
 */

@RestController
public class TestController {

    @Value("${foo}")
    String foo;

    @RequestMapping(value = "/foo")
    public String test() {
        return foo;
    }
}
