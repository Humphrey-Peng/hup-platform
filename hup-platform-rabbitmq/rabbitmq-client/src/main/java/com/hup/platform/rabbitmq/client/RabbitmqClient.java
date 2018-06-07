package com.hup.platform.rabbitmq.client;

import com.hup.platform.rabbitmq.server.controller.RabbitmqRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "platform-rabbitmq")
@RequestMapping(value = "/message")
public interface RabbitmqClient {

    @PostMapping
    void sendMessage(@RequestBody RabbitmqRequest.Message message);
}
