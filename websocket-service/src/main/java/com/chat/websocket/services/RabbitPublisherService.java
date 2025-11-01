package com.chat.websocket.services;

import com.chat.websocket.config.RabbitMqConfig;
import com.chat.websocket.dto.LastSeenDto;
import com.chat.websocket.dto.messages.MessageDto;
import com.chat.websocket.dto.status.StatusDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitPublisherService {

    RabbitTemplate rabbitTemplate;

    //process in coming messages
    public void sendToProcess(MessageDto messageDto){
        System.out.println("sendToProcess triggered");
        rabbitTemplate.convertAndSend(RabbitMqConfig.MESSAGE_EXCHANGE, RabbitMqConfig.MESSAGE_PROCESS_QUEUE, messageDto);
    }

    //process message statuses
    public void updateMsgStatus(StatusDto statusDto){
        System.out.println("updateMsgStatus triggered");
        rabbitTemplate.convertAndSend(RabbitMqConfig.MESSAGE_EXCHANGE,RabbitMqConfig.MESSAGE_STATUS_PROCESS_QUEUE,statusDto);
    }

    public void updateLastSeen(LastSeenDto lastSeenDto){
        rabbitTemplate.convertAndSend(RabbitMqConfig.LAST_SEEN_EXCHANGE,RabbitMqConfig.LAST_SEEN_QUEUE,lastSeenDto);
    }

}
