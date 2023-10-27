package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.model.dto.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/message/{room}")
    public void receiveMessage(@Payload Message message, @DestinationVariable String room) {
       simpMessagingTemplate.convertAndSend("/chat-rooms/" + room, message);
    }
}
