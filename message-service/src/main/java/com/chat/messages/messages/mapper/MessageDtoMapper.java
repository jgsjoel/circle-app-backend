package com.chat.messages.messages.mapper;

import com.chat.messages.messages.dto.MessageDto;
import com.chat.messages.messages.entities.Message;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class MessageDtoMapper {

    public static MessageDto toDto(Message message){
        MessageDto messageDto = new MessageDto();
        messageDto.setMessageId(message.getId());
        messageDto.setFromId(message.getFromId());
        messageDto.setSentAt(message.getSentAt().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")));
        messageDto.setIsRead(message.getIsRead());
        return messageDto;
    }

}
