package com.hup.platform.rabbitmq.server.service;


import com.hup.platform.rabbitmq.model.RabbitmqRequest;

public interface SenderService {

    void send(RabbitmqRequest.Message message);
}
