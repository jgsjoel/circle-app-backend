package com.chat.messages.messages.dto;

import com.chat.messages.messages.enums.MessageType;
import lombok.Data;

import java.util.List;


@Data
public class ResponseWrapDto<T> {

    private MessageType type;
    private List<T> content;

}
