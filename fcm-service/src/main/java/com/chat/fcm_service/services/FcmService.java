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

    public void saveToken(String userId, String token) {
        if(userHasToken(userId,token)){
            log.info("FCM token for userId {} is already up to date.", userId);
        } else {
            log.info("Saving FCM token for userId {}: {}", userId, token);
            FcmToken existingToken = fcmRepo.findByUserId(userId);
            if (existingToken != null) {
                existingToken.setToken(token);
                fcmRepo.save(existingToken);
                log.info("Updated existing FCM token for userId {}", userId);
            } else {
                FcmToken newFcmToken = new FcmToken();
                newFcmToken.setUserId(userId);
                newFcmToken.setToken(token);
                fcmRepo.save(newFcmToken);
                log.info("Saved new FCM token for userId {}", userId);
            }

        }
    }

    public String getTokenByUserId(String userId) {
        log.info("Retrieving FCM token for userId: {}", userId);
        FcmToken fcmToken = fcmRepo.findByUserId(userId);
        if (fcmToken != null) {
            return fcmToken.getToken();
        }
        return null;
    }

    public boolean userHasToken(String userId,String token) {
        log.info("Checking if userId {} has an FCM token", userId);
        return fcmRepo.existsByUserIdAndToken(userId,token);
    }

}
