package com.chat.user.services;

import com.chat.user.dto.ContactDto;
import com.chat.user.dto.ContactListDto;
import com.chat.user.dto.LastSeenDto;
import com.chat.user.dto.UserDto;
import com.chat.user.entities.Image;
import com.chat.user.entities.User;
import com.chat.user.exceptions.NoSuchEntityException;
import com.chat.user.repositories.ImageRepo;
import com.chat.user.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepo userRepo;
    private ImageRepo imageRepo;

    public UserService(UserRepo userRepo,ImageRepo imageRepo){
        this.userRepo = userRepo;
        this.imageRepo = imageRepo;
    }

    public User getUserByMobile(String mobile){
        return userRepo.findByMobile(mobile).orElse(null);
    }

    public User getUserById(String userID){
        return userRepo.findById(userID).orElseThrow(()-> new NoSuchEntityException("Invalid User"));
    }

    public User createUser(UserDto userDto){

        User user = new User();
        user.setName(userDto.getName());
        user.setMobile(userDto.getMobile());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepo.save(user);

    }

    public User updateUser(User user,UserDto userDto){
        user.setName(userDto.getName());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepo.save(user);
    }

    public void deleteUser(String id){
        try {
            userRepo.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("No user found with id: {}", id);
        }
    }

    private String getUserImageUrl(User user) {
        return imageRepo.findByUserId(user.getId())
                .map(image -> "/images/" + image.getId())
                .orElse(null);
    }

    public ContactListDto getUsersByContactNumber(ContactListDto input) {
        List<ContactDto> requestContacts = input.getContacts();
        System.out.println("-----------got triggered-------------");

        // Get all phone numbers
        List<String> phoneNumbers = requestContacts.stream()
                .map(ContactDto::getPhone)
                .toList();

        System.out.println(phoneNumbers);

        // Fetch users from DB
        List<User> users = userRepo.findByMobileIn(phoneNumbers);
        System.out.println("Fetched Users:");
        System.out.println(users);

        // Build matched contacts, replacing name with request name
        List<ContactDto> matchedContacts = users.stream().map(user -> {

            ContactDto dto = new ContactDto();
            dto.setPhone(user.getMobile());
            dto.setPublicId(user.getId());
            System.out.println(getUserImageUrl(user));
            dto.setImageUrl(getUserImageUrl(user));

            // Find the request contact with the same phone
            requestContacts.stream()
                    .filter(c -> c.getPhone().equals(user.getMobile()))
                    .findFirst()
                    .ifPresent(c -> dto.setName(c.getName()));

            // fallback to DB name if no match in request
            if (dto.getName() == null) {
                dto.setName(user.getName());
            }

            return dto;
        }).toList();

        System.out.println(matchedContacts);

        ContactListDto result = new ContactListDto();
        result.setContacts(matchedContacts);

        return result;
    }

    public void updateLastSeen(LastSeenDto lastSeenDto) {
        userRepo.findById(lastSeenDto.getUserId())
                .ifPresent(user -> {
                    user.setLastSeen(lastSeenDto.getLastSeen());
                    userRepo.save(user);
                });
    }

    public LastSeenDto getLastSeenByUserId(String userId) {
        return userRepo.findById(userId)
                .map(user -> {
                    LastSeenDto lastSeenDto = new LastSeenDto();
                    lastSeenDto.setLastSeen(user.getLastSeen());
                    lastSeenDto.setUserId(userId);
                    return lastSeenDto;
                })
                .orElse(null);
    }

    public List<Map<String, String>> getContactImagesByPublicIds(List<String> publicIds) {
        // Fetch users by IDs
        List<Image> images = imageRepo.findByUserIdIn(publicIds);

        // Build response list
        List<Map<String, String>> lst = images.stream().map(image -> {
            System.out.println("------------data-------------");
            System.out.println(image.getUser().getId());
            System.out.println(image.getUrl());
            return Map.of(
                    "public_id", image.getUser().getId(),
                    "image_url", image.getUrl()
            );
        }).collect(Collectors.toList());

        System.out.println("Contact images response:");
        System.out.println(lst);

        return lst;
    }




}
