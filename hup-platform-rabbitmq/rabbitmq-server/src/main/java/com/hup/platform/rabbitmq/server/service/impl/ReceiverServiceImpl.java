package com.hup.platform.rabbitmq.server.service.impl;

import com.hup.platform.rabbitmq.server.service.ReceiverService;
import org.springframework.stereotype.Service;

@Service
public class ReceiverServiceImpl implements ReceiverService {

    @Override
    public void business(String message) {
        System.out.println(message + "业务处理完成！！");
    }
}
