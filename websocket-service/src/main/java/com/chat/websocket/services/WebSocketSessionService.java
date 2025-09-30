package com.chat.websocket.services;

import com.chat.websocket.entities.WebSocketUserSession;
import com.chat.websocket.repos.WebSocketSessionRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WebSocketSessionService {

    private final WebSocketSessionRepo repository;

    public void addSession(String userId, String sessionId) {
        WebSocketUserSession wsSession = new WebSocketUserSession(userId, sessionId);
        repository.save(wsSession);
    }

    public WebSocketUserSession getSession(String userId) {
        return repository.findById(userId).orElse(null);
    }

    public void removeSession(String userId) {
        repository.deleteById(userId);
    }

}
