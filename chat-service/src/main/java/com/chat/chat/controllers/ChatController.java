package com.chat.chat.controllers;

import com.chat.chat.services.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
@AllArgsConstructor
public class ChatController {

    private ChatService chatService;

    @GetMapping("/sender/{senderId}/{receiverId}")
    public void getChatDetails(@PathVariable String senderId, @PathVariable String receiverId) {
        chatService.getOrCreatePrivateChat(senderId,receiverId);
    }


}
