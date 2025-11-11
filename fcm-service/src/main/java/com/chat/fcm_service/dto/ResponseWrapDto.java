package com.chat.fcm_service.dto;

import com.chat.fcm_service.enums.MessageType;
import lombok.Data;

@Data
public class ResponseWrapDto<T> {

    private MessageType type;
    private T content;

}
