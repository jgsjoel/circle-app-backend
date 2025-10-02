package com.chat.websocket.dto;

import com.chat.websocket.enums.MessageStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MsgStatUpdate {

    @NotBlank
    @JsonProperty("message_id")
    private String messageId;
    @NotBlank
    @JsonProperty("sender_id")
    private String senderId;
    @NotBlank
    @JsonProperty("receiver_id")
    private String receiverId;

    @NotNull(message = "Status must be one of SENT, RECEIVED, READ")
    @JsonProperty("status")
    private MessageStatus status;



}
