package com.chat.websocket.services;

import com.chat.websocket.enums.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UndeliveredMessageService {

    private final RedisTemplate<String, Object> redisTemplate;

    private String getKey(String userId, MessageType type) {
        return "undelivered:" + userId + ":" + type.name().toLowerCase();
    }

    public void addUndeliveredMessage(String userId, String messageId, MessageType type) {
        redisTemplate.opsForList().rightPush(getKey(userId, type), messageId);
        redisTemplate.expire(getKey(userId, type), Duration.ofHours(1)); // optional TTL
    }

    @SuppressWarnings("unchecked")
    public List<String> getUndeliveredMessages(String userId, MessageType type) {
        List<Object> list = redisTemplate.opsForList().range(getKey(userId, type), 0, -1);
        return list == null ? List.of() : list.stream().map(Object::toString).collect(Collectors.toList());
    }

    public void clearUndeliveredMessages(String userId, MessageType type) {
        redisTemplate.delete(getKey(userId, type));
    }
}
