package com.chat.websocket.dto;

import com.chat.websocket.enums.MessageStatus;
import lombok.Data;

@Data
public class SenderRespDto {

    private String pubMsgId;
    private MessageStatus messageStatus;

}
