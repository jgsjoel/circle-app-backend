package com.chat.websocket.dto.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InComingMsgStruct<T> {

    @JsonProperty("msg_type")
    private String messageType;
    private T message;

}
