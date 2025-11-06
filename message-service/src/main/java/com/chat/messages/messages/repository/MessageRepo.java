package com.chat.messages.messages.repository;

import com.chat.messages.messages.entities.Message;
import com.chat.messages.messages.enums.MessageStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message,String> {

    @EntityGraph(attributePaths = {"mediaFiles"})
    List<Message> findAllByRecipientsUserIdAndStatus(
            String userId,
            MessageStatus status
    );


    List<Message> findAllByRecipientsUserIdAndStatusAndSentAtAfter(
            String userId,
            MessageStatus status,
            LocalDateTime since
    );

}
