package com.chat.messages.messages.services;

import com.chat.messages.messages.dto.MessageDto;
import com.chat.messages.messages.dto.IncomingMessageDto;
import com.chat.messages.messages.entities.MediaFile;
import com.chat.messages.messages.entities.Message;
import com.chat.messages.messages.mapper.MessageDtoMapper;
import com.chat.messages.messages.repository.MediaFileRepo;
import com.chat.messages.messages.repository.MessageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private MessageRepo messageRepo;
    private ChatService chatService;
    private MediaFileRepo mediaFileRepo;


    //TODO: in case of a group chat use only chat id to validate
    // in case of a 1 to 1 use sender, receiver to validate
    public String validateChat(){
      chatService.getChatDetails();
    }

    public MessageDto saveMessage(IncomingMessageDto saveDto) {

        String chatId = validateChat(); // You need to implement this

        Message message = new Message();
        message.setChatId(chatId);
        message.setMessage(saveDto.getMessage());
        message.setFromId(saveDto.getSenderId());

        Message newMessage = messageRepo.save(message);

        List<MediaFile> savedMediaFiles = List.of();

//        if (saveDto.getMediaDtos() != null && !saveDto.getMediaDtos().isEmpty()) {
//            savedMediaFiles = saveFileData(newMessage, saveDto.getMediaDtos());
//        }

        return MessageDtoMapper.toDto(newMessage, savedMediaFiles,saveDto.getSenderId());
    }


//    private List<MediaFile> saveFileData(Message message, List<MediaDto> mediaDtoList) {
//        List<MediaFile> savedFiles = mediaDtoList.stream().map(mediaDto -> {
//            MediaFile mediaFile = new MediaFile();
//            mediaFile.setUrl(mediaDto.getUrl());
//            mediaFile.setPublicId(mediaDto.getPublicId());
//            mediaFile.setMessage(message);
//            return mediaFileRepo.save(mediaFile);
//        }).toList();
//
//        return savedFiles;
//    }

    //TODO: before getting signed url for images make sure that a chat is created
    public void getSignedUrl(){
        validateChat();
    }


    public void DeleteMessageById(){

    }

    public Object getMessagesForChatId(String id) {
    }

    public void deleteMessageWithId(String id) {
    }
}
