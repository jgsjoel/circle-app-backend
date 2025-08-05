package com.chat.user.mapper;

import com.chat.user.dto.UserDto;
import com.chat.user.entities.User;

public class UserDtoMapper {

    public static UserDto toUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setMobile(user.getMobile());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        return userDto;
    }

}
