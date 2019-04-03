package com.zan.hu.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @version 1.0
 * @Author hupeng
 * @Date 2019-04-03 21:01
 * @Description todo
 **/
@SpringCloudApplication
public class ZipkinApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinApplication.class, args);
    }
}
