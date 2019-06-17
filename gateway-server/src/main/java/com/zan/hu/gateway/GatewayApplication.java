package com.zan.hu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @version 1.0
 * @Author hupeng
 * @Date 2019-04-03 21:05
 * @Description todo
 **/
@SpringCloudApplication
@ComponentScan("com.zan.hu")
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
