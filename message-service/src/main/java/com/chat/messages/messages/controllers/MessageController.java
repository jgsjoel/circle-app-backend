package com.chat.messages.messages.controllers;

import com.chat.messages.messages.dto.messages.ReceiverRespDo;
import com.chat.messages.messages.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private MessageService messageService;

    @GetMapping("/unsent-messages/{id}")
    public ResponseEntity<List<ReceiverRespDo>> getUnsentMessagesById(@PathVariable("id") String userId) {
        List<ReceiverRespDo> response = messageService.getUnSentMessagesForUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
