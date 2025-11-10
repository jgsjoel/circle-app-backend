package com.chat.fcm_service.services;

import com.chat.fcm_service.config.RabbitMqConfig;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitConsumerService {


    @RabbitListener(queues = RabbitMqConfig.MESSAGE_PROCESS_QUEUE)
    public void listenToMessageProcessResponse() {

    }



}
