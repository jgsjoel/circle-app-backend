package com.chat.messages.messages.repository;

import com.chat.messages.messages.entities.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipientRepo extends JpaRepository<Recipient,String> {

}
