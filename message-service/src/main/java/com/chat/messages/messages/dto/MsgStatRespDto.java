package com.chat.messages.messages.dto;

import com.chat.messages.messages.enums.MessageStatus;
import lombok.Data;

@Data
public class MsgStatRespDto {

    private String pubMsgId;
    private MessageStatus messageStatus;

}
