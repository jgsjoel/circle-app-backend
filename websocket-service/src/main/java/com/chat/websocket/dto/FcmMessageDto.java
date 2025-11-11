package com.chat.websocket.dto;

import com.chat.websocket.dto.messages.ReceiverRespDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcmMessageDto<T> {
    private String to;
    private ResponseWrapDto<T> payload;
}
