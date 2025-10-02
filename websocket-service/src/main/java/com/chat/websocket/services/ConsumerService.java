package com.chat.websocket.services;

import com.chat.websocket.config.RabbitMqConfig;
import com.chat.websocket.dto.MsgSendRespDto;
import com.chat.websocket.dto.ReceiverRespDo;
import com.chat.websocket.dto.MsgStatRespDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsumerService {

    private SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(queues = RabbitMqConfig.MESSAGE_RESPONSE_QUEUE)
    public void listenToMessageProcessResponse(MsgSendRespDto<MsgStatRespDto, ReceiverRespDo> msgSendRespDto){
        //receiver first
        simpMessagingTemplate.convertAndSend("/topic/"+msgSendRespDto.getReceiverId(), msgSendRespDto.getReceiver());
        //sender at last
        simpMessagingTemplate.convertAndSend("/topic/"+msgSendRespDto.getSenderId(), msgSendRespDto.getSender());
    }

    @RabbitListener(queues = RabbitMqConfig.MESSAGE_STATUS_RESPONSE_QUEUE)
    public void listenToMsgStatResponse(MsgSendRespDto<MsgStatRespDto, MsgStatRespDto> msgSendRespDto){
        //receiver first
        simpMessagingTemplate.convertAndSend("/topic/"+msgSendRespDto.getReceiverId(), msgSendRespDto.getReceiver());
        //sender at last
        simpMessagingTemplate.convertAndSend("/topic/"+msgSendRespDto.getSenderId(), msgSendRespDto.getSender());
    }

}
