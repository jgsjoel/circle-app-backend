package com.chat.websocket.entities;

import com.chat.websocket.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("UndeliveredMessage")
public class UndeliveredMessage implements Serializable {

    @Id
    private String userId;

    private List<String> messageIds;
    private MessageType messageType;
}