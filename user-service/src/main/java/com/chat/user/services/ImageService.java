package com.chat.user.services;

import com.chat.user.dto.ImageUploadDto;
import com.chat.user.dto.UserDto;
import com.chat.user.entities.Image;
import com.chat.user.entities.User;
import com.chat.user.exceptions.NoSuchEntityException;
import com.chat.user.repositories.ImageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ImageService {

    public final ImageRepo imageRepo;

    public Image saveOrUpdateImage(ImageUploadDto imageUploadDto, User user){
        Image image = imageRepo.findByUserId(user.getId()).orElse(null);
        if (image == null){
            Image newImage = new Image();
            newImage.setUrl(imageUploadDto.getUrl());
            newImage.setUser(user);
            newImage.setPublicId(imageUploadDto.getPublicId());
            return imageRepo.save(newImage);
        }else{
            image.setUrl(imageUploadDto.getUrl());
            image.setUser(user);
            image.setPublicId(imageUploadDto.getPublicId());
            return imageRepo.save(image);
        }
    }

}
