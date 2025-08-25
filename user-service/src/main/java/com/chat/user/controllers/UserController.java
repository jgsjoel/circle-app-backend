package com.chat.user.controllers;

import com.chat.user.dto.ContactDto;
import com.chat.user.dto.ContactListDto;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
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
    public ResponseEntity<?> isUserById(@PathVariable String id){
        User user = userService.getUserById(id);
        if (user != null){
            return new ResponseEntity<>(UserDtoMapper.toUserDto(user),HttpStatus.OK);
        }else{
            return new ResponseEntity<>("No Such User",HttpStatus.NOT_FOUND);
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

    @PostMapping("/sync-contacts")
    public ResponseEntity<byte[]> syncContacts(@RequestBody byte[] binaryData) {
        try {
            String json = new String(binaryData, StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            System.out.println(json);

            // Parse as list first
            List<ContactDto> contacts = mapper.readValue(json, new TypeReference<List<ContactDto>>() {});
            System.out.println(json);
            // Wrap in ContactListDto for your service
            ContactListDto contactListDto = new ContactListDto();
            contactListDto.setContacts(contacts);

            ContactListDto matchedContacts = userService.getUsersByContactNumber(contactListDto);

            byte[] responseBytes = mapper.writeValueAsBytes(matchedContacts);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                    .body(responseBytes);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON", e);
        }
    }


}


