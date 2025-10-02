package com.chat.messages.messages.services;

import com.chat.messages.messages.dto.*;
import com.chat.messages.messages.entities.MediaFile;
import com.chat.messages.messages.entities.Message;
import com.chat.messages.messages.enums.MessageStatus;
import com.chat.messages.messages.mapper.MessageDtoMapper;
import com.chat.messages.messages.repository.MediaFileRepo;
import com.chat.messages.messages.repository.MessageRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {

    private MessageRepo messageRepo;
    private ChatService chatService;
    private MediaFileRepo mediaFileRepo;

    //TODO: in case of a group chat use only chat id to validate
    // in case of a 1 to 1 use sender, receiver to validate
    public Mono<ChatResponseDto> validateChat(String senderID, String receiverID){
      return chatService.getChatDetails(senderID, receiverID);
    }

    public MsgSendRespDto saveMessage(MessageDto messageDto) {

        ChatResponseDto chatResponseDto  = validateChat(
                messageDto.getSenderId(),
                messageDto.getReceiverId()
        ).block();

        if (chatResponseDto == null) {
            log.warn("Empty Chat Response, skipping message. Sender: {}, Receiver: {}",
                    messageDto.getSenderId(), messageDto.getReceiverId());
            return null;
        }

        Message message = new Message();
        message.setChatId(chatResponseDto.getChatId());
        message.setMessage(messageDto.getMessage());
        message.setFromId(chatResponseDto.getSenderID());
        message.setStatus(MessageStatus.SENT);

        Message newMessage = messageRepo.save(message);

        MediaFile savedMediaFile = null;

        if (messageDto.getMediaUrl() != null && !messageDto.getMediaUrl().isEmpty()) {
            savedMediaFile = saveFileData(newMessage,messageDto.getMediaUrl(),messageDto.getMediaPublicId());
        }

        return MessageDtoMapper.toMsgRespDto(newMessage,savedMediaFile, chatResponseDto);
    }


    private MediaFile saveFileData(Message newMsg, String mediaUri,String mediaPubId) {
        MediaFile mediaFile = new MediaFile();
        mediaFile.setUrl(mediaUri);
        mediaFile.setPublicId(mediaPubId);
        mediaFile.setMessage(newMsg);
        return mediaFileRepo.save(mediaFile);
    }

    public MsgSendRespDto<MsgStatRespDto, MsgStatRespDto> updateStatus(MsgStatUpdate msgStatUpdate) {
        //TODO: update the
        return MessageDtoMapper.toMsgStatRespDto(msgStatUpdate);
    }

    //TODO: before getting signed url for images make sure that a chat is created
//    public void getSignedUrl(){
//        validateChat();
//    }


}
