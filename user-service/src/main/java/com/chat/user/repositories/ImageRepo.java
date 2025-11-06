package com.chat.user.repositories;

import com.chat.user.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageRepo extends JpaRepository<Image,String> {

    Optional<Image> findByUserId(String id);

    List<Image> findByUserIdIn(List<String> userIds);
}
