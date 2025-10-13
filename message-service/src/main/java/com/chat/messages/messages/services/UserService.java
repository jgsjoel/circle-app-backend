package com.chat.messages.messages.services;

import com.chat.messages.messages.dto.LastSeenDto;
import lombok.extern.slf4j.Slf4j;
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

    public Mono<LastSeenDto> getUserLastSeen(String userId) {
        return webClient.get()
                .uri("/last-seen/{id}", userId)
                .retrieve()
                .bodyToMono(LastSeenDto.class)
                .onErrorResume(WebClientResponseException.NotFound.class, e -> Mono.empty());
    }

}
