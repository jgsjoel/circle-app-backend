package com.chat.messages.messages.services;

import com.chat.messages.messages.config.RabbitConfig;
import com.chat.messages.messages.dto.ChatResponseDto;
import com.chat.messages.messages.dto.MsgSendRespDto;
import com.chat.messages.messages.dto.ReceiverRespDo;
import com.chat.messages.messages.dto.SenderRespDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.sound.midi.Receiver;

@Service
@AllArgsConstructor
public class PublisherService {

    private RabbitTemplate rabbitTemplate;

    public void sendToMessageProcessResponse(MsgSendRespDto<SenderRespDto, ReceiverRespDo> msgSendRespDto){
        rabbitTemplate.convertAndSend(RabbitConfig.MESSAGE_EXCHANGE, RabbitConfig.MESSAGE_RESPONSE_QUEUE, msgSendRespDto);
        System.out.println("sending from message service: "+msgSendRespDto.toString());
    }

}
