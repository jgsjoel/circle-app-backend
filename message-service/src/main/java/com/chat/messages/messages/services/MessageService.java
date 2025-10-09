package com.chat.messages.messages.services;

import com.chat.messages.messages.dto.*;
import com.chat.messages.messages.entities.MediaFile;
import com.chat.messages.messages.entities.Message;
import com.chat.messages.messages.entities.Recipient;
import com.chat.messages.messages.enums.MessageStatus;
import com.chat.messages.messages.mapper.MessageDtoMapper;
import com.chat.messages.messages.repository.MediaFileRepo;
import com.chat.messages.messages.repository.MessageRepo;
import com.chat.messages.messages.repository.RecipientRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {

    private MessageRepo messageRepo;
    private ChatService chatService;
    private MediaFileRepo mediaFileRepo;
    private RecipientRepo recipientRepo;
    private UserService userService;

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
        message.setSenderMobile(chatResponseDto.getSenderMobile());

        Message newMessage = messageRepo.save(message);

        //save the recipient
        Recipient recipient = new Recipient();
        recipient.setMessage(newMessage);
        recipient.setUserId(chatResponseDto.getReceiverId());
        recipientRepo.save(recipient);

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

    public List<MsgSendRespDto> getUnSentMessagesForUser(String userId) {
        System.out.println("here");
        // Fetch messages where the user is a recipient and status is SENT
        List<Message> unSentMessages = messageRepo.findAllByRecipientsUserIdAndStatus(
                userId,
                MessageStatus.SENT
        );

        for (Message msg : unSentMessages) {
            System.out.println("id: " + msg.getId() +
                    ", senderId: " + msg.getFromId() +
                    ", content: " + msg.getMessage() +
                    ", status: " + msg.getStatus());
        }

        // Map to response DTOs
        return unSentMessages.stream()
                .map(message -> {
                    var media = message.getMediaFiles().isEmpty() ? null : message.getMediaFiles().get(0);
                    return MessageDtoMapper.toMsgRespDto(message, media, null);
                })
                .toList();
    }


}
