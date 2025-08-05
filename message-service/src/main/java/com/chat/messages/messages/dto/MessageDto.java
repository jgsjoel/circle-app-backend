package com.chat.messages.messages.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDto {

    private String messageId;
    private String fromId;
    private String sentAt;
    private Boolean isRead;

}
