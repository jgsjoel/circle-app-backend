package com.chat.user.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageUploadDto {

    @NotEmpty(message = "Url Cannot be empty")
    private String url;
    @NotNull(message = "Public ID cannot be empty")
    private String publicId;

}
