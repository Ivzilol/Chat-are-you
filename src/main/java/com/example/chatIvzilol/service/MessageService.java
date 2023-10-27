package com.example.chatIvzilol.service;


import com.example.chatIvzilol.model.dto.Message;
import com.example.chatIvzilol.model.entity.ChatMessage;
import com.example.chatIvzilol.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ChatMessageRepository messageRepository;

    public MessageService(ChatMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveMessage(Message message) {
        ChatMessage chatMessage = new ChatMessage();
    }
}
