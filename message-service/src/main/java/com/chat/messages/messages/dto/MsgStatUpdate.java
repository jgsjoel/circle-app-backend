package com.chat.messages.messages.dto;

import com.chat.messages.messages.enums.MessageStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @JsonProperty("status")
    private MessageStatus status;

}
