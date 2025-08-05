package com.chat.chat.services;

import com.chat.chat.dtos.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service").build();
    }

    public Mono<UserDto> getUserByMobile(String mobile){
        return webClient.get()
                .uri("/users/mobile/{id}",mobile)
                .retrieve()
                .bodyToMono(UserDto.class);
    }



}
