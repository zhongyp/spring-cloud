package com.zhongyp.rabbitmq.service.config;

import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author yupeng chung <yupengchung@gmail.com>
 * @version 1.0
 * @date 2020/3/19
 * @since jdk1.8
 */
@Component
public class SysConfig implements ApplicationListener<WebServerInitializedEvent> {
    public int getServerPort() {
        return serverPort;
    }

    private int serverPort;

    public String getUrl() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "http://" + address.getHostAddress() + ":" + this.serverPort;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
    }

}
