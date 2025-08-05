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

}
