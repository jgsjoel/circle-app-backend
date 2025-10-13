package com.chat.messages.messages.dto.status;

import com.chat.messages.messages.enums.MessageStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatusDto {

    @JsonProperty("message_id")
    private String messageId;
    @JsonProperty("updated_by_id")
    private String updatedById;
    private MessageStatus status;

}
