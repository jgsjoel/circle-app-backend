package com.chat.user.controllers;

import com.chat.user.dto.ImageUploadDto;
import com.chat.user.dto.UserDto;
import com.chat.user.entities.Image;
import com.chat.user.entities.User;
import com.chat.user.groups.Create;
import com.chat.user.groups.UpdateName;
import com.chat.user.mapper.ImageDtoMapper;
import com.chat.user.mapper.UserDtoMapper;
import com.chat.user.services.CloudinaryService;
import com.chat.user.services.ImageService;
import com.chat.user.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ImageService imageService;

    //works
    @GetMapping("/{id}")
    public ResponseEntity<?> isUserExistent(@PathVariable String id){
        User user = userService.getUserById(id);
        if (user != null){
            return new ResponseEntity<>(UserDtoMapper.toUserDto(user),HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No Such User",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/mobile/{mobile}")
    public ResponseEntity<?> getByMobile(@PathVariable String mobile){
        User user = userService.getUserByMobile(mobile);
        if (user != null){
            return new ResponseEntity<>(UserDtoMapper.toUserDto(user),HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No Such User",HttpStatus.BAD_REQUEST);
        }
    }


    //this is triggered only by the api gateway
    @PostMapping("/create-update")
    public ResponseEntity<UserDto> createUser(@Validated(Create.class) @RequestBody UserDto userDto){

        User user = userService.getUserByMobile(userDto.getMobile());
        if(user == null){
            System.out.println("create method");
            return new ResponseEntity<UserDto>(UserDtoMapper.toUserDto(userService.createUser(userDto)), HttpStatus.CREATED);
        }else{
            System.out.println("update method");
            return new ResponseEntity<UserDto>(UserDtoMapper.toUserDto(userService.updateUser(user,userDto)), HttpStatus.CREATED);
        }
    }

    //works
    @PostMapping("/update-name")
    public ResponseEntity<UserDto> updateName(@Validated(UpdateName.class) @RequestBody UserDto userDto,@RequestHeader("X-User-Id") String userId){

        User user = userService.getUserById(userId);
        User update = userService.updateUser(user,userDto);
        System.out.println(update.getName());
        System.out.println(update.getUpdatedAt());
        System.out.println(update.getMobile());
        return ResponseEntity.ok(UserDtoMapper.toUserDto(update));
    }

    //works
    @PostMapping("/image-save-update")
    public ResponseEntity<ImageUploadDto> imageSaveUpdate(@Valid @RequestBody ImageUploadDto imgDto,@RequestHeader("X-User-Id") String userId){

        User user = userService.getUserById(userId);
        Image image = imageService.saveOrUpdateImage(imgDto,user);
        return ResponseEntity.ok(ImageDtoMapper.toImageDto(image));

    }

    //works
    @GetMapping("/signed-url")
    public ResponseEntity<Map<String, Object>> get(@RequestHeader("X-User-Id") String userId){
        return new ResponseEntity<>(cloudinaryService.getUploadSignature(userId),HttpStatus.OK);
    }

    //works
    @DeleteMapping("/")
    public ResponseEntity<?> deleteUser(@RequestHeader("X-User-Id") String userId){

        userService.deleteUser(userId);
        return new ResponseEntity<String>("User Has Been Deleted", HttpStatus.CREATED);

    }

}


