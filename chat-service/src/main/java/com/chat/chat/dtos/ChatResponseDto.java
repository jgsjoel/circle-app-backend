package com.chat.chat.dtos;

import lombok.Data;

@Data
public class ChatResponseDto {

    private String chatId;
    private String senderID;
    private String senderMobile;
    private String receiverId;

}
