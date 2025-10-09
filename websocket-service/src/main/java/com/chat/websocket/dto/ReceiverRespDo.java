package com.chat.websocket.dto;

import lombok.Data;

@Data
public class ReceiverRespDo {

    private String message;
    private String senderId;
    private String senderMobile;
    private String pubChatId;

}
