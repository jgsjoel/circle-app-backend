package com.chat.messages.messages.dto;

import lombok.Data;

@Data
public class ReceiverRespDo {

    private String message;
    private String senderId;
    private String senderMobile;
    private String pubChatId;

    private String mediaUrl;
    private String mediaPublicId;
    private String messageType;

}
