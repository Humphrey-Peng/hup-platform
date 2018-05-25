package com.hup.platform.rabbitmq.server.listening;


import com.hup.platform.rabbitmq.server.service.ReceiverService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues ="hello")
public class Receiver {

    @Autowired
    private ReceiverService receiverService;

    @RabbitHandler
    public void process(String message){
        receiverService.business(message);
    }
}
