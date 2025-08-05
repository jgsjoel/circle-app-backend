package com.chat.auth.services;

import com.chat.auth.dtos.RequestDto;
import com.chat.auth.dtos.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    private final WebClient webClient;

    public UserService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://user-service").build();
    }

    public Mono<UserDto> createUser(RequestDto request){
        return webClient.post()
                .uri("/users/create-update")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserDto.class);
    }

    public Mono<UserDto> getUserById(String id){
        return webClient.get()
                .uri("/users/{id}",id)
                .retrieve()
                .bodyToMono(UserDto.class);
    }



}
