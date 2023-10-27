package com.example.chatIvzilol.service;


import com.example.chatIvzilol.model.dto.Message;
import com.example.chatIvzilol.model.dto.UserDTO;
import com.example.chatIvzilol.model.entity.ChatMessage;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.repository.ChatMessageRepository;
import com.example.chatIvzilol.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageService {

    private final ChatMessageRepository messageRepository;

    private final UserRepository userRepository;

    public MessageService(ChatMessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public void saveMessage(Message message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setCreatedDate(LocalDateTime.now());
        chatMessage.setMessage(message.getMessage());
        Optional<UserDTO> user = userRepository.findUserByUsername(message.getReceiverName());

    }
}
