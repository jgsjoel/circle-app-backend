package com.chat.messages.messages.mapper;

import com.chat.messages.messages.dto.messages.MediaDto;
import com.chat.messages.messages.dto.messages.MsgSendRespDto;
import com.chat.messages.messages.dto.messages.MsgStatRespDto;
import com.chat.messages.messages.dto.messages.ReceiverRespDo;
import com.chat.messages.messages.entities.MediaFile;
import com.chat.messages.messages.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDtoMapper {

    public static MsgSendRespDto toMsgRespDto(Message message, List<MediaFile> mediaFiles, String receiverId,String senderMsgId,String senderChatId) {

        //goes to sender
        MsgStatRespDto senderRespDto = new MsgStatRespDto();
        senderRespDto.setMessageId(senderMsgId);
        senderRespDto.setPubChatId(message.getChatId());
        senderRespDto.setMessageStatus(message.getStatus());
        senderRespDto.setPubMsgId(message.getId());
        senderRespDto.setChatId(senderChatId);

        List<MediaDto> mediaDtoList = new ArrayList<>();;
        if (mediaFiles != null && !mediaFiles.isEmpty()){
            mediaFiles.forEach(mediaFile -> {
                MediaDto mediaDto = new MediaDto();
                mediaDto.setPublicId(mediaFile.getPublicId());
                mediaDto.setUrl(mediaFile.getUrl());
                mediaDtoList.add(mediaDto);
            });
        }

        //goes to receiver
        ReceiverRespDo receiverRespDo = new ReceiverRespDo();
        receiverRespDo.setPubMessageId(message.getId());
        receiverRespDo.setMediaDtoList(mediaDtoList);
        receiverRespDo.setMessage(message.getMessage());
        receiverRespDo.setSenderId(message.getFromId());
        receiverRespDo.setPubChatId(message.getChatId());
        receiverRespDo.setSenderMobile(message.getSenderMobile());

        //the actual final obj
        MsgSendRespDto msgSendRespDto = new MsgSendRespDto();
        msgSendRespDto.setSenderId(message.getFromId());
        msgSendRespDto.setReceiverId(receiverId);
        msgSendRespDto.setSender(senderRespDto);
        msgSendRespDto.setReceiver(receiverRespDo);

        return  msgSendRespDto;
    }

    public static ReceiverRespDo toReceiverRespDo(Message message, List<MediaFile> mediaFiles) {

        List<MediaDto> mediaDtoList = new ArrayList<>();
        if (mediaFiles != null && !mediaFiles.isEmpty()){
            mediaFiles.forEach(mediaFile -> {
                MediaDto mediaDto = new MediaDto();
                mediaDto.setPublicId(mediaFile.getPublicId());
                mediaDto.setUrl(mediaFile.getUrl());
                mediaDtoList.add(mediaDto);
            });
        }

        ReceiverRespDo receiverRespDo = new ReceiverRespDo();
        receiverRespDo.setPubMessageId(message.getId());
        receiverRespDo.setMediaDtoList(mediaDtoList);
        receiverRespDo.setMessage(message.getMessage());
        receiverRespDo.setSenderId(message.getFromId());
        receiverRespDo.setPubChatId(message.getChatId());
        receiverRespDo.setSenderMobile(message.getSenderMobile());

        return  receiverRespDo;

    }
}
