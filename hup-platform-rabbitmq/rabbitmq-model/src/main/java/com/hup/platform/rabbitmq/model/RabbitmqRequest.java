package com.hup.platform.rabbitmq.model;

import lombok.Data;

public interface RabbitmqRequest {

    @Data
    class Message {
        private String content;
    }
}
