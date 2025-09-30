package com.chat.websocket.services;

import com.chat.websocket.config.RabbitMqConfig;
import com.chat.websocket.dto.MessageDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublisherService {

    RabbitTemplate rabbitTemplate;

    public void sendToProcess(MessageDto messageDto){
        rabbitTemplate.convertAndSend(RabbitMqConfig.MESSAGE_EXCHANGE, RabbitMqConfig.MESSAGE_PROCESS_QUEUE, messageDto);
    }


}
