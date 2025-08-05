package com.chat.auth.dtos;

import com.chat.auth.groups.Request;
import com.chat.auth.groups.Validate;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    @NotEmpty(message = "The user name cannot be  empty",groups = {Validate.class})
    private String name;
    @NotEmpty(message = "The user name cannot be  empty",groups = {Validate.class, Request.class})
    private String mobile;
    @NotEmpty(message = "The user name cannot be  empty",groups = {Validate.class})
    private String otp;

}
