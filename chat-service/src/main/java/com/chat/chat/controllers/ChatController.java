package com.chat.chat.controllers;

import com.chat.chat.dtos.ChatResponseDto;
import com.chat.chat.services.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {

    private ChatService chatService;

    @GetMapping("/get-create/{senderId}/{receiverId}")
    public ResponseEntity<ChatResponseDto> getChatDetails(@PathVariable String senderId, @PathVariable String receiverId) {
        System.out.println("here");
        return new ResponseEntity<ChatResponseDto>(chatService.getOrCreatePrivateChat(senderId,receiverId), HttpStatus.OK);
    }


}
