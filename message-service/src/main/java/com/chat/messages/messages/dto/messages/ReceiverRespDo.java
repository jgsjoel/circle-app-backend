package com.chat.messages.messages.dto.messages;

import lombok.Data;

import java.util.List;

@Data
public class ReceiverRespDo {

    private String messageId;
    private String message;
    private String senderId;
    private String senderMobile;
    private String pubChatId;
    private List<MediaDto> mediaDtoList;

}
