package com.hup.platform.rabbitmq.server.config;

import com.hup.platform.rabbitmq.server.common.QueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Bean
    public Queue queue() {
        return new Queue(QueueConstant.HELLO);
    }

}
