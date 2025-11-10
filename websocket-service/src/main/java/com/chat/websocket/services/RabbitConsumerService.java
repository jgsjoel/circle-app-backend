package com.chat.websocket.services;

import com.chat.websocket.config.RabbitMqConfig;
import com.chat.websocket.dto.ResponseWrapDto;
import com.chat.websocket.dto.messages.MsgSendRespDto;
import com.chat.websocket.dto.messages.ReceiverRespDo;
import com.chat.websocket.dto.messages.MsgStatRespDto;
import com.chat.websocket.dto.status.StatusDto;
import com.chat.websocket.dto.status.StatusUpdateRespDto;
import com.chat.websocket.enums.MessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitConsumerService {

    private final ChatWebSocketHandler chatWebSocketHandler;
    UndeliveredMessageService undeliveredMessageService;
    RabbitPublisherService rabbitPublisherService;

    @RabbitListener(queues = RabbitMqConfig.MESSAGE_RESPONSE_QUEUE)
    public void listenToMessageProcessResponse(MsgSendRespDto<MsgStatRespDto, ReceiverRespDo> msgSendRespDto) {

        // Send to receiver
        ResponseWrapDto<ReceiverRespDo> receiverRespDoResponseWrapDto = new ResponseWrapDto<>();
        receiverRespDoResponseWrapDto.setContent(msgSendRespDto.getReceiver());
        receiverRespDoResponseWrapDto.setType(MessageType.MESSAGE);
        boolean receiverSent = chatWebSocketHandler.sendMessageToUser(msgSendRespDto.getReceiverId(),receiverRespDoResponseWrapDto);
        if (!receiverSent) {
            undeliveredMessageService.addUndeliveredMessage(msgSendRespDto.getReceiverId(),msgSendRespDto.getReceiver().getPubMessageId(),MessageType.MESSAGE);
            //***************** Trigger FCM notification ****************//
            rabbitPublisherService.sendToFcm();
            System.out.println("⚠️ Receiver not connected: " + msgSendRespDto.getReceiverId());
        }

        // Send to sender
        ResponseWrapDto<MsgStatRespDto> msgStatRespDtoResponseWrapDto = new ResponseWrapDto<>();
        msgStatRespDtoResponseWrapDto.setContent(msgSendRespDto.getSender());
        msgStatRespDtoResponseWrapDto.setType(MessageType.STATUS_UPDATE);
        boolean senderSent = chatWebSocketHandler.sendMessageToUser(msgSendRespDto.getSenderId(),msgStatRespDtoResponseWrapDto);
        if (!senderSent) {
            undeliveredMessageService.addUndeliveredMessage(msgSendRespDto.getSenderId(),msgSendRespDto.getSender().getPubMsgId(),MessageType.STATUS_UPDATE);
            rabbitPublisherService.sendToFcm();
            System.out.println("⚠️ Sender not connected: " + msgSendRespDto.getSenderId());
        }
    }

    @RabbitListener(queues = RabbitMqConfig.MESSAGE_STATUS_RESPONSE_QUEUE)
    public void listenToStatusUpdate(StatusUpdateRespDto statusUpdate) {

        System.out.println("listenToStatusUpdate: "+statusUpdate.toString());

        ResponseWrapDto<MsgStatRespDto> originResponseWrap = new ResponseWrapDto<>();
        originResponseWrap.setContent(statusUpdate.getMsgStatRespDto());
        originResponseWrap.setType(MessageType.STATUS_UPDATE);
        boolean originSent = chatWebSocketHandler.sendMessageToUser(statusUpdate.getOrigSenderId(), originResponseWrap);

        if (!originSent) {
            undeliveredMessageService.addUndeliveredMessage(
                statusUpdate.getOrigSenderId(),
                statusUpdate.getMsgStatRespDto().getMessageId(),
                MessageType.STATUS_UPDATE
            );
            System.out.println("⚠️ User not connected for status update: " + statusUpdate.getOrigSenderId());
        }

        ResponseWrapDto<MsgStatRespDto> updatedByResponseWrap = new ResponseWrapDto<>();
        updatedByResponseWrap.setContent(statusUpdate.getMsgStatRespDto());
        updatedByResponseWrap.setType(MessageType.STATUS_UPDATE);
        boolean updaterSent = chatWebSocketHandler.sendMessageToUser(statusUpdate.getUpdatedById(), updatedByResponseWrap);

        if (!updaterSent) {
            undeliveredMessageService.addUndeliveredMessage(
                    statusUpdate.getUpdatedById(),
                    statusUpdate.getMsgStatRespDto().getMessageId(),
                    MessageType.STATUS_UPDATE
            );
            System.out.println("⚠️ User not connected for status update: " + statusUpdate.getUpdatedById());
        }
    }

}
