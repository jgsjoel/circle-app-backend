package com.chat.websocket.services;

import com.chat.websocket.dto.LastSeenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service").build();
    }

    public Mono<LastSeenDto> getLastSeen(String userId) {
        return webClient.get()
                .uri("/last-seen/{id}", userId)
                .retrieve()
                .bodyToMono(LastSeenDto.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                if (ex.getStatusCode() == HttpStatus.NOT_FOUND || ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    log.warn("User not found or invalid ID: {}", userId);
                    return Mono.empty();
                }
                return Mono.error(ex);
        });
    }






}
