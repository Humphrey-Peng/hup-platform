package com.hup.platform.rabbitmq.service.impl;

import com.hup.platform.rabbitmq.controller.RabbitmqRequest;
import com.hup.platform.rabbitmq.service.SenderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SenderServiceImpl implements SenderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(RabbitmqRequest.Message message) {
        this.rabbitTemplate.convertAndSend("hello",message.getContent());
    }
}
