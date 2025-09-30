package com.chat.messages.messages.services;

import com.chat.messages.messages.config.RabbitConfig;
import com.chat.messages.messages.dto.MessageDto;
import com.chat.messages.messages.dto.MsgSendRespDto;
import com.chat.messages.messages.dto.ReceiverRespDo;
import com.chat.messages.messages.dto.SenderRespDto;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsumerService {

    private MessageService messageService;
    private PublisherService publisherService;

    @RabbitListener(queues = {RabbitConfig.MESSAGE_PROCESS_QUEUE})
    public void consumeMessage(MessageDto messageDto){
        MsgSendRespDto<SenderRespDto, ReceiverRespDo> msgSendRespDto = messageService.saveMessage(messageDto);
        publisherService.sendToMessageProcessResponse(msgSendRespDto);
    }

}
