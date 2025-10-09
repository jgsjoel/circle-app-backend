package com.chat.messages.messages.services;

import com.chat.messages.messages.config.RabbitConfig;
import com.chat.messages.messages.dto.*;
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

//    @RabbitListener(queues = {RabbitConfig.MESSAGE_STATUS_PROCESS_QUEUE})
//    public void consumeMsgStat(MsgStatUpdate msgStatUpdate){
//        try {
//            MsgSendRespDto<MsgStatRespDto, MsgStatRespDto> msgSendRespDto = messageService.updateStatus(msgStatUpdate);
//            if (msgSendRespDto != null) {
//                publisherService.sendToMsgStatResponse(msgSendRespDto);
//            } else {
//                log.warn("No status response to send for Sender: {}, Receiver: {}",
//                        msgStatUpdate.getSenderId(), msgStatUpdate.getReceiverId());
//            }
//        } catch (IllegalArgumentException e) {
//            log.warn("Empty Chat Response, skipping message. Sender: {}, Receiver: {}",
//                    msgStatUpdate.getSenderId(), msgStatUpdate.getReceiverId());
//        } catch (Exception e) {
//            log.error("Unexpected error while processing message: {}", msgStatUpdate, e);
//        }
//    }


}
