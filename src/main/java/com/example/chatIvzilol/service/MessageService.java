package com.example.chatIvzilol.service;


import com.example.chatIvzilol.model.dto.ChatMessageDTO;
import com.example.chatIvzilol.model.dto.Message;
import com.example.chatIvzilol.model.entity.ChatMessage;
import com.example.chatIvzilol.model.entity.ChatRoom;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.repository.ChatMessageRepository;
import com.example.chatIvzilol.repository.ChatRoomRepository;
import com.example.chatIvzilol.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageService {
    private final ChatMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public MessageService(ChatMessageRepository messageRepository, UserRepository userRepository, ChatRoomRepository chatRoomRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public void saveMessage(Message message, String room) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setCreatedDate(LocalDateTime.now());
        chatMessage.setMessage(message.getMessage());
        Optional<User> user = userRepository.findByUsername(message.getSenderName());
        chatMessage.setMessageCreator(user.get());
        ChatRoom chatRoom = this.chatRoomRepository.findByUniqueCode(room);
        chatMessage.setChatRoom(chatRoom);
        this.messageRepository.save(chatMessage);
    }

    public Set<ChatMessageDTO> getAllMessagesFromRoom(String room, User user) {
        return this.messageRepository.findByRoom(room);
    }
}
