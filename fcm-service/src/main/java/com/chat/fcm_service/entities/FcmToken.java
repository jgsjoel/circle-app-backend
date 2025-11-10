package com.chat.fcm_service.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class FcmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String token;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;

    @PrePersist
    protected void onCreate() {
        CreatedAt = LocalDateTime.now();
        UpdatedAt = LocalDateTime.now();
    }

}
