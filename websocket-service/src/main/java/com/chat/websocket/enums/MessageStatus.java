package com.chat.websocket.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MessageStatus {
    SENT,
    RECEIVED,
    READ;

    @JsonCreator
    public static MessageStatus fromString(String value) {
        if (value == null) return null;
        try {
            return MessageStatus.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // will fail @NotNull validation
        }
    }
}
