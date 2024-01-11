package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.model.dto.ChatMessageDTO;
import com.example.chatIvzilol.model.dto.Message;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    public ChatController(SimpMessagingTemplate simpMessagingTemplate, MessageService messageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
    }
    @MessageMapping("/message/{room}")
    public synchronized void receiveMessage(@Payload Message message, @DestinationVariable String room) {
        simpMessagingTemplate.convertAndSend("/chat-rooms/" + room, message);
        synchronized (this) {
            if (message.getMessage() != null) {
                messageService.saveMessage(message, room);
            }
        }
    }
    @GetMapping("api/message/get-messages/{room}")
    public ResponseEntity<?> getMessagesFromChat(@PathVariable String room,
                                                 @AuthenticationPrincipal User user) {
        Set<ChatMessageDTO> chatMessageDTO = this.messageService.getAllMessagesFromRoom(room, user);
        return ResponseEntity.ok(chatMessageDTO);
    }
}
