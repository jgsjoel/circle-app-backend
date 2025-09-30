package com.chat.messages.messages.dto;

import com.chat.messages.messages.enums.MessageStatus;
import lombok.Data;

@Data
public class SenderRespDto {

    private String pubMsgId;
    private MessageStatus messageStatus;

}
