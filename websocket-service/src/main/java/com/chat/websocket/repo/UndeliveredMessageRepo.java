package com.chat.websocket.repo;

import com.chat.websocket.entities.UndeliveredMessage;
import com.chat.websocket.enums.MessageType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UndeliveredMessageRepo extends CrudRepository<UndeliveredMessage, String> {

    List<UndeliveredMessage> findByIdAndMessageType(String userId, MessageType messageType);
}

