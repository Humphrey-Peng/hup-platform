package com.hup.platform.rabbitmq.client;

import com.hup.platform.rabbitmq.controller.RabbitmqRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

//@FeignClient(name = "")
@RequestMapping(value = "/message")
public interface RabbitmqClient {

    @PostMapping
    void sendMessage(@RequestBody RabbitmqRequest.Message message);
}
