package com.chat.fcm_service.repos;

import com.chat.fcm_service.entities.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmRepo extends JpaRepository<FcmToken, Long> {

    FcmToken findByUserId(String userId);

    boolean existsByUserIdAndToken(String userId,String token);

}
