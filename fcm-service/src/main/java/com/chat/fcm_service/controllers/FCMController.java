package com.chat.fcm_service.controllers;

import com.chat.fcm_service.dto.TokenDto;
import com.chat.fcm_service.services.FcmService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fcm")
@AllArgsConstructor
@Slf4j
public class FCMController {

    private FcmService fcmService;

    @PostMapping("/save-token")
    public ResponseEntity<?> uploadMedia(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody TokenDto tokenDto
            ) {
        log.info("Received FCM token for userId {}: {}", userId, tokenDto.getFcmToken());
        fcmService.SaveToken(userId, tokenDto.getFcmToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
