package com.hup.platform.rabbitmq.service;

import com.hup.platform.rabbitmq.controller.RabbitmqRequest;

public interface SenderService {

    void send(RabbitmqRequest.Message message);
}
