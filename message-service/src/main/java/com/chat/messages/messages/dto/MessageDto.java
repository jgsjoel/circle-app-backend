package com.chat.messages.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private String Id;
    private String message;
    private String senderId;
    private String receiverId;
    private String chatId;
    private List<MediaDto> mediaDtoList;

}
