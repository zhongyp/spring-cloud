package com.zhongyp.spring.cloud.ribbon.client.service.impl;

import com.zhongyp.spring.cloud.ribbon.client.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhongyp.
 * @date 2019/12/24
 */
@Service
public class RibbonServiceImpl implements RibbonService {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String sayHi() {
        return restTemplate.getForObject("http://PROVIDER/user/1", String.class);
    }

}
