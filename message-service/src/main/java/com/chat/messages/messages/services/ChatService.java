package com.chat.messages.messages.services;

import com.chat.chat.dtos.UserDto;
import com.chat.messages.messages.dto.ChatResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class ChatService {

    private final WebClient webClient;

    public ChatService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://chat-service").build();
    }

    public Mono<ChatResponseDto> getChatDetails(String senderID, String receiverID) {
        return webClient.get()
                .uri("/sender/{senderId}/{receiverId}", senderID,receiverID)
                .retrieve()
                .bodyToMono(ChatResponseDto.class)
                .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty());
    }






}
