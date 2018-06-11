package com.hup.platform.rabbitmq.server.controller;

import com.hup.platform.rabbitmq.model.RabbitmqRequest;
import com.hup.platform.rabbitmq.server.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/message")
public class RabbitmqController {

    @Autowired
    private SenderService senderService;

    @PostMapping
    public void sendMessage(@RequestBody RabbitmqRequest.Message message){
        senderService.send(message);
    }
}
