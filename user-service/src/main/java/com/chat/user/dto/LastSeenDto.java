package com.chat.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LastSeenDto {

    private String userId;
    private LocalDateTime lastSeen;

}
