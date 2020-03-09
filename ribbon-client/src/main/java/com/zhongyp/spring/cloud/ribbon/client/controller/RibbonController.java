package com.zhongyp.spring.cloud.ribbon.client.controller;

import com.zhongyp.spring.cloud.ribbon.client.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhongyp.
 * @date 2019/12/24
 */
@RestController
@RequestMapping("/ribbon")
public class RibbonController {

    @Autowired
    private RibbonService ribbonService;

    @RequestMapping("/test")
    public String test() {
        return ribbonService.sayHi();
    }
}
