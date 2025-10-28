package com.chat.messages.messages.controllers;

import com.chat.messages.messages.dto.messages.ReceiverRespDo;
import com.chat.messages.messages.services.CloudinaryService;
import com.chat.messages.messages.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;
    private CloudinaryService cloudinaryService;

    @GetMapping("/unsent-messages/{id}")
    public ResponseEntity<List<ReceiverRespDo>> getUnsentMessagesById(@PathVariable("id") String userId) {
        List<ReceiverRespDo> response = messageService.getUnSentMessagesForUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


//    @PostMapping("/signed-url/{chatId}")
//    public ResponseEntity<Map<String, Object>> uploadMedia(
//            @RequestHeader("X-User-Id") String userId,
//            @PathVariable("chatId") String chatId,
//            @RequestBody Map<String, Object> body
//    ) {
//        // you can read the files list from body if needed:
//        System.out.println(body.get("files"));
//
//        return ResponseEntity.ok(cloudinaryService.getUploadSignature(userId, chatId));
//    }

    @PostMapping("/signed-url/{chatId}")
    public ResponseEntity<?> uploadMedia(
            @RequestHeader("X-User-Id") String userId,
            @PathVariable("chatId") String chatId,
            @RequestBody Map<String, Object> body
    ) {
        List<Map<String, Object>> files = (List<Map<String, Object>>) body.get("files");

        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No files provided"));
        }

        List<Map<String, String>> signatures = cloudinaryService.getUploadSignatures(userId, chatId, files);

        return ResponseEntity.ok(Map.of("urls", signatures));
    }


}
