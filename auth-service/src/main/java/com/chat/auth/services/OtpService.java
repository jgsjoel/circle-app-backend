package com.chat.auth.services;

import com.chat.auth.dtos.RequestDto;
import com.chat.auth.dtos.UserDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    @Value("${otp.ttl}")
    private long otpTtl;
    private final WebClient webClient;
    @Value("${otp.endpoint}")
    private String otpEndpoint;
    @Value("${otp.auth-token}")
    private String authToken;
    @Value("${otp.message-template}")
    private String messageTemplate;
    @Value("${otp.sender-id}")
    private String senderId;
    @Value("${spring.redis.host}")
    private String host;

    @PostConstruct
    public void init() {
        System.out.println("otp.ttl loaded: " + otpTtl);
        System.out.println("redis host: "+host);
    }


    public OtpService(RedisTemplate<String, String> redisTemplate,@Qualifier("externalWebClient") WebClient webClient) {
        this.redisTemplate = redisTemplate;
        this.webClient = webClient;
    }

    public void saveOtp(String phoneNumber, String otp) {
        String key = buildKey(phoneNumber);
        redisTemplate.opsForValue().set(key, otp, otpTtl, TimeUnit.MINUTES);
    }

    public boolean validateOtp(String phoneNumber, String inputOtp) {
        String key = buildKey(phoneNumber);
        String storedOtp = redisTemplate.opsForValue().get(key);

        return storedOtp != null && storedOtp.equals(inputOtp);
    }

    public void deleteOtp(String phoneNumber) {
        redisTemplate.delete(buildKey(phoneNumber));
    }

    private String buildKey(String phoneNumber) {
        return "otp:" + phoneNumber;
    }

    public int generateOtp(){
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }

    public Mono<String> sendMessage(String receiverMobile, String otp) {
        System.out.println("send message triggered!");
        System.out.println("endpoint: "+otpEndpoint);
        System.out.println("token: "+authToken);
        System.out.println("message template: "+messageTemplate);
        System.out.println("sender id: "+senderId);
        System.out.println("receiver mobile: "+receiverMobile);
        System.out.println("receiver otp: "+otp);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("recipient", receiverMobile);
        requestBody.put("sender_id", senderId);
        requestBody.put("type", "plain");
        requestBody.put("message", String.format("%s %s", messageTemplate, otp));

        return webClient.post()
                .uri(otpEndpoint)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> {
                    System.out.println("Failed to send SMS: " + e.getMessage());
                });
    }

}

