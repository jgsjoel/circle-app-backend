package com.chat.messages.messages.mapper;

import com.chat.messages.messages.dto.MediaDto;
import com.chat.messages.messages.dto.MessageDto;
import com.chat.messages.messages.entities.MediaFile;
import com.chat.messages.messages.entities.Message;

import java.util.List;
import java.util.stream.Collectors;

public class MessageDtoMapper {

    public static MessageDto toDto(Message message, List<MediaFile> mediaFiles,String senderId) {
        List<MediaDto> mediaDtos = mediaFiles.stream()
                .map(mediaFile -> new MediaDto(mediaFile.getUrl(), mediaFile.getPublicId()))
                .collect(Collectors.toList());

        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setMessage(message.getMessage());
        messageDto.setSenderId(message.getFromId());
        messageDto.setReceiverId(""); // you can set this if needed
        messageDto.setChatId(message.getChatId());
        messageDto.setMediaDtoList(mediaDtos);

        return messageDto;
    }
}
