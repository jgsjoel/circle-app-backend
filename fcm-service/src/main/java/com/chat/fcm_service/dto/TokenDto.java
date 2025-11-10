package com.chat.fcm_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDto {

    @JsonProperty("fcm_token")
    private String fcmToken;

}
