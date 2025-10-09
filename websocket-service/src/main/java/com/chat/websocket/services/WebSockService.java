package com.chat.websocket.services;

import com.chat.websocket.dto.LastSeenDto;
import com.chat.websocket.dto.ReceiverRespDo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class WebSockService {

    private MessageService messageService;
    private SimpMessagingTemplate simpMessagingTemplate;

    public void getUnsentMessagesByUserId(String userId){
        System.out.println("-----------getUnsentMessagesByUserId triggered------------");
        List<ReceiverRespDo> messages = messageService.getUnsentMessagesByLastSeen(userId).block();
        System.out.println(messages);
        simpMessagingTemplate.convertAndSend("/topic/unsent."+userId,messages);
    }


}
