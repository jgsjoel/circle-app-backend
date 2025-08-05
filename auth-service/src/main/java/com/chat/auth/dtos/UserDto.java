package com.chat.auth.dtos;

import com.chat.auth.groups.Validate;
import com.chat.auth.groups.Request;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String id;
    private String name;
    private String mobile;
    private String otp;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
