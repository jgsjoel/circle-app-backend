package com.chat.chat.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Chat {

    @Id
    private String id;

    private String chatName;
    private Boolean isGroup;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "chat",cascade = CascadeType.ALL)
    private List<ChatParticipant> chatParticipants;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }


}
