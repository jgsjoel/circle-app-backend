package com.chat.user.dto;

import com.chat.user.groups.Create;
import com.chat.user.groups.Delete;
import com.chat.user.groups.OtpValidate;
import com.chat.user.groups.UpdateName;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {

    private String id;
    @NotEmpty(message = "Name Cannot Be Empty",groups = {Create.class, UpdateName.class})
    private String name;
    @NotEmpty(message = "Mobile Cannot Be Empty",groups = {Create.class, OtpValidate.class})
    private String mobile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
