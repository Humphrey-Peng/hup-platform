package com.hup.platform.rabbitmq.server.controller;

import lombok.Data;

public interface RabbitmqRequest {

    @Data
    class Message {
        private String content;
    }
}
