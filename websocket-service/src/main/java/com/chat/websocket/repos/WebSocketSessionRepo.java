package com.chat.websocket.repos;

import com.chat.websocket.entities.WebSocketUserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebSocketSessionRepo extends CrudRepository<WebSocketUserSession, String> {
}
