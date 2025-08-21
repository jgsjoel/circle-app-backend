package com.chat.user.services;

import com.chat.user.dto.UserDto;
import com.chat.user.entities.User;
import com.chat.user.exceptions.NoSuchEntityException;
import com.chat.user.repositories.UserRepo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;


@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
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
            throw new NoSuchEntityException("User with ID " + id + " not found");
        }
    }

    public ContactListDto getUsersByContactNumber(ContactListDto input) {
        // Extract all phone numbers from request
        List<String> phoneNumbers = input.getContacts()
                .stream()
                .map(ContactDto::getPhone)
                .toList();

        // Find all users that exist in DB
        List<User> users = userRepo.findByMobileIn(phoneNumbers);

        // Convert to ContactDto (only existing users)
        List<ContactDto> matchedContacts = users.stream().map(user -> {
            ContactDto dto = new ContactDto();
            dto.setPhone(user.getMobile());
            dto.setName(user.getName()); // depends on your User entity
            dto.setPublicId(user.getId()); // or another unique field
            return dto;
        }).toList();

        // Wrap in ContactListDto
        ContactListDto result = new ContactListDto();
        result.setContacts(matchedContacts);

        return result;
    }
}
