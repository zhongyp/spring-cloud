package com.zhongyp.cloud.bus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/4/3
 * @since jdk1.8
 */
@org.springframework.web.bind.annotation.RestController
@RefreshScope
public class RestController {

    @Value("${foo}")
    private String foo;

    @RequestMapping("foo")
    public String foo() {

        System.out.println(foo);
        return foo;
    }
}
