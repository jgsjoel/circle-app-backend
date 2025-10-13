package com.chat.websocket.services;

import com.chat.websocket.dto.messages.ReceiverRespDo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class MessageService {

    private final WebClient webClient;

    public MessageService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://message-service").build();
    }


    public Mono<List<ReceiverRespDo>> getUnsentMessagesByLastSeen(String userId) {
        return webClient.get()
                .uri("/messages/unsent-messages/{id}", userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ReceiverRespDo>>() {})  // <-- fixed
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND || ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        log.warn("User not found or invalid ID: {}", userId);
                        return Mono.just(Collections.emptyList()); // return empty list instead of Mono.empty()
                    }
                    return Mono.error(ex);
                });
    }
}
