package com.chat.messages.messages.controllers;

import com.chat.messages.messages.dto.IncomingMessageDto;
import com.chat.messages.messages.services.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<?> saveMessage(@Valid @RequestBody IncomingMessageDto saveDto) {
        return ResponseEntity.ok(messageService.saveMessage(saveDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getMessagesForChatId(@PathVariable("id") String id) {
        return new ResponseEntity<Object>(messageService.getMessagesForChatId(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMessageWithId(@PathVariable("id") String id) {
        messageService.deleteMessageWithId(id);
        return ResponseEntity.noContent().build();
    }

}
