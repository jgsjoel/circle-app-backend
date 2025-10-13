package com.chat.messages.messages.services;

import com.chat.messages.messages.config.RabbitConfig;
import com.chat.messages.messages.dto.messages.MsgSendRespDto;
import com.chat.messages.messages.dto.messages.ReceiverRespDo;
import com.chat.messages.messages.dto.messages.MsgStatRespDto;
import com.chat.messages.messages.dto.status.StatusUpdateRespDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublisherService {

    private RabbitTemplate rabbitTemplate;

    public void sendToMessageProcessResponse(MsgSendRespDto<MsgStatRespDto, ReceiverRespDo> msgSendRespDto){
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_EXCHANGE, RabbitConfig.MESSAGE_RESPONSE_QUEUE, msgSendRespDto);
        System.out.println("sending from message service: "+msgSendRespDto.toString());
    }

    public void sendToMsgStatResponse(StatusUpdateRespDto statusUpdateRespDto){
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_EXCHANGE, RabbitConfig.MESSAGE_STATUS_RESPONSE_QUEUE, statusUpdateRespDto);
        System.out.println("sending from message service: "+statusUpdateRespDto.toString());
    }

}
