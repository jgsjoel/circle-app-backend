package com.chat.messages.messages.mapper;

import com.chat.messages.messages.dto.*;
import com.chat.messages.messages.entities.MediaFile;
import com.chat.messages.messages.entities.Message;
import com.chat.messages.messages.enums.MessageStatus;

public class MessageDtoMapper {

    public static MsgSendRespDto toMsgRespDto(Message message, MediaFile mediaFile, ChatResponseDto chatResponseDto) {

        MsgStatRespDto senderRespDto = new MsgStatRespDto();
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

    public static MsgSendRespDto<MsgStatRespDto,MsgStatRespDto> toMsgStatRespDto(MsgStatUpdate msgStatUpdate) {

        MsgStatRespDto senderRespDto = new MsgStatRespDto();
        senderRespDto.setMessageStatus(msgStatUpdate.getStatus());
        senderRespDto.setPubMsgId(msgStatUpdate.getMessageId());

        MsgStatRespDto receiverRespDto = new MsgStatRespDto();
        receiverRespDto.setMessageStatus(msgStatUpdate.getStatus());
        receiverRespDto.setPubMsgId(msgStatUpdate.getMessageId());

        MsgSendRespDto msgSendRespDto = new MsgSendRespDto();
        msgSendRespDto.setSenderId(msgStatUpdate.getSenderId());
        msgSendRespDto.setReceiverId(msgStatUpdate.getReceiverId());
        msgSendRespDto.setSender(senderRespDto);
        msgSendRespDto.setReceiver(receiverRespDto);

        return  msgSendRespDto;


    }
}
