package com.chat.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContactDto {

    private String phone;
    private String name;
    @JsonProperty("public_id")
    private String publicId;

}
