package com.chat.websocket.dto.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    @NotBlank
    @JsonProperty("message_id")
    private String messageId;
    private String message;
    @NotBlank
    @JsonProperty("sender_id")
    private String senderId;
    @NotBlank
    @JsonProperty("receiver_id")
    private String receiverId;
    @NotBlank
    @JsonProperty("sender_timestamp")
    private String senderTimeStamp;
    @NotBlank
    @JsonProperty("message_type")
    private String messageType;
    @JsonProperty("media_list")
    private List<MediaDto> mediaList;

}
