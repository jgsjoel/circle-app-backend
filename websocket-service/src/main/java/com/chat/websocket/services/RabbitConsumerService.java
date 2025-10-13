package com.chat.websocket.services;

import com.chat.websocket.config.RabbitMqConfig;
import com.chat.websocket.dto.messages.MsgSendRespDto;
import com.chat.websocket.dto.messages.ReceiverRespDo;
import com.chat.websocket.dto.messages.MsgStatRespDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitConsumerService {

    private final ChatWebSocketHandler chatWebSocketHandler;

    @RabbitListener(queues = RabbitMqConfig.MESSAGE_RESPONSE_QUEUE)
    public void listenToMessageProcessResponse(MsgSendRespDto<MsgStatRespDto, ReceiverRespDo> msgSendRespDto) {

        // Send to receiver
        boolean receiverSent = chatWebSocketHandler.sendMessageToUser(
                msgSendRespDto.getReceiverId(),
                msgSendRespDto.getReceiver()
        );
        if (!receiverSent) {
            System.out.println("⚠️ Receiver not connected: " + msgSendRespDto.getReceiverId());
        }

        // Send to sender
        boolean senderSent = chatWebSocketHandler.sendMessageToUser(
                msgSendRespDto.getSenderId(),
                msgSendRespDto.getSender()
        );
        if (!senderSent) {
            System.out.println("⚠️ Sender not connected: " + msgSendRespDto.getSenderId());
        }
    }

}
