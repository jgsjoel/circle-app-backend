package com.chat.messages.messages.services;

import com.chat.messages.messages.dto.messages.*;
import com.chat.messages.messages.dto.status.StatusDto;
import com.chat.messages.messages.dto.status.StatusUpdateRespDto;
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

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {

    private MessageRepo messageRepo;
    private ChatService chatService;
    private MediaFileRepo mediaFileRepo;
    private RecipientRepo recipientRepo;

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

        List<MediaFile> savedMediaFiles = null;
        System.out.println(messageDto.getMediaList());

        if (messageDto.getMediaList() != null && !messageDto.getMediaList().isEmpty()) {
            savedMediaFiles = new ArrayList<>();
            for(MediaDto mediaDto: messageDto.getMediaList()){
                savedMediaFiles.add(saveFileData(newMessage,mediaDto.getUrl(),mediaDto.getPublicId()));
            }
        }

        return MessageDtoMapper.toMsgRespDto(newMessage,savedMediaFiles, chatResponseDto.getReceiverId(),messageDto.getMessageId(),messageDto.getChatId());
    }

    private MediaFile saveFileData(Message newMsg, String mediaUri,String mediaPubId) {
        MediaFile mediaFile = new MediaFile();
        mediaFile.setUrl(mediaUri);
        mediaFile.setPublicId(mediaPubId);
        mediaFile.setMessage(newMsg);
        return mediaFileRepo.save(mediaFile);
    }

    public List<ReceiverRespDo> getUnSentMessagesForUser(String userId) {
        List<Message> unSentMessages = messageRepo.findAllByRecipientsUserIdAndStatus(
                userId,
                MessageStatus.SENT
        );

        for (Message msg : unSentMessages) {
            log.info("id: {}, senderId: {}, content: {}, status: {}, mediaCount: {}",
                    msg.getId(), msg.getFromId(), msg.getMessage(),
                    msg.getStatus(), msg.getMediaFiles().size());
        }

        return unSentMessages.stream()
                .map(message -> {
                    List<MediaFile> media = message.getMediaFiles().isEmpty() ? null : message.getMediaFiles();
                    return MessageDtoMapper.toReceiverRespDo(message, media);
                })
                .toList();
    }

    public StatusUpdateRespDto statusUpdate(StatusDto status){
        Message message = messageRepo.findById(status.getMessageId()).orElse(null);
        if(message != null){
            message.setStatus(status.getStatus());
            messageRepo.save(message);
        }

        return new StatusUpdateRespDto(status.getUpdatedById(), message.getFromId(), status);
    }


}
