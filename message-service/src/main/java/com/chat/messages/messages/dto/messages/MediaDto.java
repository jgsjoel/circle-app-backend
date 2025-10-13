package com.chat.messages.messages.dto.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MediaDto {

    private String url;
    @JsonProperty("public_id")
    private String publicId;

}
