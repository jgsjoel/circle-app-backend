package com.chat.websocket.config;

import com.chat.websocket.dto.LastSeenDto;
import com.chat.websocket.services.RabbitPublisherService;
import com.chat.websocket.services.WebSockService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class WebSockEventListener {

    private final RabbitPublisherService publisherService;
    private WebSockService webSockService;

    @EventListener
    public void handleWebSocketConnect(SessionConnectEvent event) {
        // Optionally store userId here
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = headerAccessor.getFirstNativeHeader("userId");
        if (userId != null) {
            headerAccessor.getSessionAttributes().put("userId", userId);
        }
    }

    @EventListener
    public void handleWebSocketSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        // Check if this is the user’s personal queue/topic
        if (destination != null && destination.contains("/topic/unsent."+userId)) {
            webSockService.getUnsentMessagesByUserId(userId);
        }
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");

        System.out.println(userId);
        if (userId != null) {
            LastSeenDto lastSeenDto = new LastSeenDto();
            lastSeenDto.setLastSeen(LocalDateTime.now());
            lastSeenDto.setUserId(userId);
            publisherService.updateLastSeen(lastSeenDto);
        }
    }



}
