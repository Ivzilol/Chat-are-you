package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.model.dto.Message;
import com.example.chatIvzilol.service.MessageService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final MessageService messageService;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, MessageService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }

    @MessageMapping("/message/{room}")
    public void receiveMessage(@Payload Message message, @DestinationVariable String room) {
       simpMessagingTemplate.convertAndSend("/chat-rooms/" + room, message);
       messageService.saveMessage(message);
    }
}
