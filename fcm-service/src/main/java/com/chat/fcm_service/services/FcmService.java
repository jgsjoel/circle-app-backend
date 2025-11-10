package com.chat.fcm_service.services;

import com.chat.fcm_service.entities.FcmToken;
import com.chat.fcm_service.repos.FcmRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FcmService {

    private FcmRepo fcmRepo;

    public void SaveToken(String userId, String token) {
        FcmToken fcmToken = new FcmToken();
        fcmToken.setUserId(userId);
        fcmToken.setToken(token);
        fcmRepo.save(fcmToken);
        log.info("Saved FCM token for userId: {}", userId);
    }

    public String getTokenByUserId(String userId) {
        log.info("Retrieving FCM token for userId: {}", userId);
        FcmToken fcmToken = fcmRepo.findByUserId(userId);
        if (fcmToken != null) {
            return fcmToken.getToken();
        }
        return null;
    }

}
