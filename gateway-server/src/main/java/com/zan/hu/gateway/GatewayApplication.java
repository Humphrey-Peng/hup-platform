package com.zan.hu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @version 1.0
 * @Author hupeng
 * @Date 2019-04-03 21:05
 * @Description todo
 **/
@SpringCloudApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
