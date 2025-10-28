package com.chat.websocket.dto;

import com.chat.websocket.enums.MessageType;
import lombok.Data;

@Data
public class ResponseWrapDto<T> {

    private MessageType type;
    private T content;

}
