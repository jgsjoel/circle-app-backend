package com.chat.user.mapper;

import com.chat.user.dto.ImageUploadDto;
import com.chat.user.dto.UserDto;
import com.chat.user.entities.Image;
import com.chat.user.entities.User;

public class ImageDtoMapper {

    public static ImageUploadDto toImageDto(Image image){
        ImageUploadDto imageUploadDto = new ImageUploadDto();
        imageUploadDto.setPublicId(image.getPublicId());
        imageUploadDto.setUrl(image.getUrl());
        return imageUploadDto;
    }

}
