package com.chat.chat.services;

import com.chat.chat.dtos.ChatResponseDto;
import com.chat.chat.dtos.UserDto;
import com.chat.chat.entities.Chat;
import com.chat.chat.entities.ChatParticipant;
import com.chat.chat.enums.Role;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import repos.ChatParticipantsRepo;
import repos.ChatRepo;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ChatService {

    private ChatRepo chatRepo;
    private ChatParticipantsRepo chatParticipantsRepo;
    private UserService userService;


    @Transactional
    public ChatResponseDto getOrCreatePrivateChat(String sender, String receiver) {
        // Validate both users exist
        UserDto senderUser = userService.getUserById(sender).block();
        UserDto receiverUser = userService.getUserById(receiver).block();

        if (senderUser == null || receiverUser == null) {
            throw new IllegalArgumentException("One or both users do not exist");
        }

        // Sort IDs so chatId is predictable
        String firstID = sender.compareTo(receiver) < 0 ? sender : receiver;
        String secondID = firstID.equals(sender) ? receiver : sender;

        // Check if chat already exists
        List<ChatParticipant> chatParticipants = chatParticipantsRepo.findAllByUserId(firstID);
        String chatId = null;
        for (ChatParticipant chatParticipant : chatParticipants) {
            if (chatParticipantsRepo.existByChatIdAndUserId(chatParticipant.getChat().getId(), secondID)) {
                chatId = chatParticipant.getChat().getId();
                break;
            }
        }

        // If no chat exists, create one
        if (chatId == null) {
            chatId = UUID.randomUUID().toString();
            Chat chat = new Chat();
            chat.setId(chatId);
            chat.setIsGroup(false);
            chat.setChatName(firstID + ":" + secondID);
            Chat newPrivateChat = chatRepo.save(chat);

            createNewChatParticipant(firstID, newPrivateChat);
            createNewChatParticipant(secondID, newPrivateChat);
        }

        // Build and return the ChatResponseDto
        ChatResponseDto response = new ChatResponseDto();
        response.setChatId(chatId);
        response.setSenderID(senderUser.getId());
        response.setSenderMobile(senderUser.getMobile());
        response.setReceiverId(receiverUser.getId());

        return response;
    }

    private void createNewChatParticipant(String firstId, Chat newChat){

        ChatParticipant participant = new ChatParticipant();
        participant.setChat(newChat);
        participant.setUserId(firstId);
        participant.setRole(Role.MEMBER);
        chatParticipantsRepo.save(participant);
    }




}
