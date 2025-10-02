package com.chat.websocket.controllers;

import com.chat.websocket.dto.MessageDto;
import com.chat.websocket.dto.MsgStatUpdate;
import com.chat.websocket.services.PublisherService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class StompController {

    private PublisherService publisherService;

    @MessageMapping("/message-pub")
    public void pubToMsgService(MessageDto messageDto){
        publisherService.sendToProcess(messageDto);
    }

    @MessageMapping("/status-update")
    public void updMsgStatus(@Valid MsgStatUpdate msgStatUpdate){
        publisherService.updateMsgStatus(msgStatUpdate);
    }

}
