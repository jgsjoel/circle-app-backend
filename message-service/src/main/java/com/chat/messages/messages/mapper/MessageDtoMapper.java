package com.chat.messages.messages.mapper;

import com.chat.messages.messages.dto.*;
import com.chat.messages.messages.entities.MediaFile;
import com.chat.messages.messages.entities.Message;

public class MessageDtoMapper {

    public static MsgSendRespDto toDto(Message message, MediaFile mediaFile, ChatResponseDto chatResponseDto) {

        SenderRespDto senderRespDto = new SenderRespDto();
        senderRespDto.setMessageStatus(message.getStatus());
        senderRespDto.setPubMsgId(message.getId());

        ReceiverRespDo receiverRespDo = new ReceiverRespDo();
        receiverRespDo.setMessage(message.getMessage());
        receiverRespDo.setSenderId(message.getFromId());
        receiverRespDo.setPubChatId(message.getChatId());
        receiverRespDo.setSenderMobile(chatResponseDto.getSenderMobile());

        MsgSendRespDto msgSendRespDto = new MsgSendRespDto();
        msgSendRespDto.setSenderId(message.getFromId());
        msgSendRespDto.setReceiverId(chatResponseDto.getReceiverId());
        msgSendRespDto.setSender(senderRespDto);
        msgSendRespDto.setReceiver(receiverRespDo);

        return  msgSendRespDto;


    }
}
