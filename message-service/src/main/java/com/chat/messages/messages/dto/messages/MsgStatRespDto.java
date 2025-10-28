package com.chat.messages.messages.dto.messages;

import com.chat.messages.messages.enums.MessageStatus;
import lombok.Data;

@Data
public class MsgStatRespDto {

    private String messageId;
    private String pubMsgId;
    private String chatId;
    private String pubChatId;
    private MessageStatus messageStatus;

}
