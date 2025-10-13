package com.chat.websocket.services;

import com.chat.websocket.dto.LastSeenDto;
import com.chat.websocket.dto.messages.InComingMsgStruct;
import com.chat.websocket.dto.messages.MessageDto;
import com.chat.websocket.dto.messages.ReceiverRespDo;
import com.chat.websocket.dto.status.StatusDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // Maintain connected users
    private final ConcurrentHashMap<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private MessageService messageService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private RabbitPublisherService publisherService;

    @PostConstruct
    private void sout(){
        System.out.println("PostConstruct ChatWebSocketHandler");
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = session.getHandshakeHeaders().getFirst("X-User-Id");

        if (userId == null) {
            try {
                session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing X-User-Id"));
            } catch (IOException ignored) {}
            return;
        }

        sessions.put(userId, session);
        System.out.println("✅ Connected user: " + userId);

        System.out.println("-----------getUnsentMessagesByUserId triggered------------");
        List<ReceiverRespDo> messages = messageService.getUnsentMessagesByLastSeen(userId).block();
        if (messages != null && !messages.isEmpty()) {
            sendMessageToUser(userId, messages);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        String userId = getUserId(session);

        System.out.printf("📩 Received message from %s: %s%n", userId, payload);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InComingMsgStruct incoming = objectMapper.readValue(payload, InComingMsgStruct.class);

            switch (incoming.getMessageType()) {
                case "message" -> {
                    MessageDto messageDto = objectMapper.convertValue(incoming.getMessage(), MessageDto.class);
                    publisherService.sendToProcess(messageDto);
                }
                case "status" -> {
                    StatusDto statusDto = objectMapper.convertValue(incoming.getMessage(), StatusDto.class);
                    publisherService.updateMsgStatus(statusDto);
                }
                default -> session.sendMessage(new TextMessage("❌ Unknown message type"));
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to parse message: " + e.getMessage());
            session.sendMessage(new TextMessage("❌ Invalid message format"));
        }

        // Example: echo back only to sender
        session.sendMessage(new TextMessage("Echo: " + payload));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = getUserId(session);
        if (userId != null) {
            publisherService.updateLastSeen(new LastSeenDto(userId, LocalDateTime.now()));
            sessions.remove(userId);
            System.out.println("❌ User disconnected: " + userId);
        }
    }

    private String getUserId(WebSocketSession session) {
        return session.getHandshakeHeaders().getFirst("X-User-Id");
    }

    /**
     * Send a message to a specific user by userId.
     */
    public boolean sendMessageToUser(String userId, Object messageObj) {
        WebSocketSession session = sessions.get(userId);
        if (session == null || !session.isOpen()) {
            System.out.println("⚠️ Cannot send message, user not connected: " + userId);
            return false;
        }

        try {
            String json = objectMapper.writeValueAsString(messageObj);
            session.sendMessage(new TextMessage(json));
            System.out.println("📤 Sent message to " + userId + ": " + json);
            return true;
        } catch (IOException e) {
            System.err.println("❌ Failed to send message to " + userId + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Check if a user is currently connected.
     */
    public boolean isUserConnected(String userId) {
        WebSocketSession session = sessions.get(userId);
        return session != null && session.isOpen();
    }

    /**
     * Get current connected user count (optional utility).
     */
    public int getConnectedUserCount() {
        return sessions.size();
    }
}
