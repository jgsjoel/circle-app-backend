package com.chat.messages.messages.services;

import com.chat.messages.messages.config.RabbitConfig;
import com.chat.messages.messages.dto.messages.MessageDto;
import com.chat.messages.messages.dto.messages.MsgSendRespDto;
import com.chat.messages.messages.dto.messages.MsgStatRespDto;
import com.chat.messages.messages.dto.messages.ReceiverRespDo;
import com.chat.messages.messages.dto.status.StatusDto;
import com.chat.messages.messages.dto.status.StatusUpdateRespDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ConsumerService {

    private MessageService messageService;
    private PublisherService publisherService;

    @RabbitListener(queues = {RabbitConfig.MESSAGE_PROCESS_QUEUE})
    public void consumeMessage(MessageDto messageDto){
        try {
            MsgSendRespDto<MsgStatRespDto, ReceiverRespDo> msgSendRespDto = messageService.saveMessage(messageDto);
            if (msgSendRespDto != null) {
                System.out.println("message id"+messageDto.getMessageId());
                publisherService.sendToMessageProcessResponse(msgSendRespDto);
            } else {
                log.warn("No message response to send for Sender: {}, Receiver: {}",
                        messageDto.getSenderId(), messageDto.getReceiverId());
            }
        } catch (IllegalArgumentException e) {
            log.warn("Empty Chat Response, skipping message. Sender: {}, Receiver: {}",
                    messageDto.getSenderId(), messageDto.getReceiverId());
        } catch (Exception e) {
            log.error("Unexpected error while processing message: {}", messageDto, e);
        }
    }

    @RabbitListener(queues = {RabbitConfig.MESSAGE_STATUS_PROCESS_QUEUE})
    public void consumeMsgStat(StatusDto statusDto){
        try {
            StatusUpdateRespDto statusUpdate = messageService.statusUpdate(statusDto);
            if (statusUpdate != null) {
                publisherService.sendToMsgStatResponse(statusUpdate);
            } else {
                log.warn("No status response to send for update by: {}, messageId: {}",
                        statusDto.getUpdatedById(), statusDto.getMessageId());
            }
        } catch (IllegalArgumentException e) {
            log.warn("Empty Chat Response, skipping message. update by: {}, message: {}",
                    statusDto.getUpdatedById(), statusDto.getMessageId());
        } catch (Exception e) {
            log.error("Unexpected error while processing message: {}", statusDto, e);
        }
    }


}
