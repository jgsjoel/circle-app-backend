package com.chat.auth.dtos;

import lombok.Data;
import lombok.Getter;

@Data
public class MqttAuthDto {

    private String username;
    private String password;

}
