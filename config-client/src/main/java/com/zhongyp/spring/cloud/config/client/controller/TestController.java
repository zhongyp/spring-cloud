package com.zhongyp.spring.cloud.config.client.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhongyp.
 * @date 2019/12/22
 */

@RestController
public class TestController {

    String foo = new String("avc");

    @RequestMapping(value = "/foo")
    public String test() {
        return foo;
    }
}
