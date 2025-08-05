package com.chat.messages.messages.services;

import com.chat.messages.messages.dto.MessageDto;
import com.chat.messages.messages.entities.Message;
import com.chat.messages.messages.repository.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class MessageService {

    private MessageRepo messageRepo;
    private WebClient webClient;

    public MessageService(MessageRepo messageRepo, WebClient webClient){
        this.messageRepo = messageRepo;
        this.webClient = webClient;
    }

    public Message saveMessage(){
        webClient.get()
                .uri("")
                .
    }

    public void DeleteMessageById(){

    }

    public List<MessageDto> getAllMessagesByChatId(){

    }

}
