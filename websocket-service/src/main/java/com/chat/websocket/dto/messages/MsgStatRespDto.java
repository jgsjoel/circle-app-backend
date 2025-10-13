package com.chat.websocket.dto.messages;

import com.chat.websocket.enums.MessageStatus;
import lombok.Data;

@Data
public class MsgStatRespDto {

    private String pubMsgId;
    private MessageStatus messageStatus;

}
