package com.chat.fcm_service.services;

import com.chat.fcm_service.config.RabbitMqConfig;
import com.chat.fcm_service.dto.FcmMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RabbitConsumerService {

    private final FcmService tokenService;
    private FirebaseMessaging firebaseMessaging;

    @RabbitListener(queues = RabbitMqConfig.MESSAGE_PROCESS_QUEUE)
    public void onFcmMessage(FcmMessageDto<?> message) throws FirebaseMessagingException {
        System.out.println("📩 Received FCM message from WebSocket: " + message);

        // Step 1: Find user's FCM token (probably stored in Redis or DB)
        String fcmToken = tokenService.getTokenByUserId(message.getTo());
        if (fcmToken == null) {
            System.out.println("⚠️ No FCM token found for user: " + message.getTo());
            return;
        }

        // Step 2: Prepare notification content
        ObjectMapper mapper = new ObjectMapper();
        String payloadJson;
        try {
            payloadJson = mapper.writeValueAsString(message.getPayload());
        } catch (Exception e) {
            System.err.println("❌ Failed to serialize payload: " + e.getMessage());
            return;
        }

        // Step 3: Send the payload as a data message (raw JSON)
        Message firebaseMsg = Message.builder()
                .setToken(fcmToken)
                .putData("payload", payloadJson)  // 🔹 sends the full payload as data
                .build();

        String response = firebaseMessaging.send(firebaseMsg);
        System.out.println("✅ FCM sent: " + response);
    }




}
