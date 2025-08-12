package com.chat.messages.messages.repository;

import com.chat.messages.messages.entities.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaFileRepo extends JpaRepository<MediaFile,String> {
}
