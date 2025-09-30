package com.chat.messages.messages.entities;

import com.chat.messages.messages.enums.MessageStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "messages")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    @Id
    private String id;
    private String chatId;
    private String message;
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    private String fromId;
    private LocalDateTime sentAt;

    @OneToMany(
            mappedBy = "message",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private List<MediaFile> mediaFiles = new ArrayList<>();  // ✅ renamed

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        sentAt = LocalDateTime.now();
    }
}
