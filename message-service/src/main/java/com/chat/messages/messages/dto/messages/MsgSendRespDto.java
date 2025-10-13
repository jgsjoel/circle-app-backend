package com.chat.messages.messages.dto.messages;

import lombok.Data;

@Data
public class MsgSendRespDto<S,R> {

    private String senderId;
    private String receiverId;
    private S sender;
    private R receiver;

}
