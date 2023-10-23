package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.model.dto.CreateChatRoomDTO;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.response.CustomResponse;
import com.example.chatIvzilol.service.ChatRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat-rooms")
@CrossOrigin(origins = {"http://localhost:3000/"}, allowCredentials = "false", allowedHeaders = "true")
public class ChatRoomsController {

    private final ChatRoomService chatRoomService;

    public ChatRoomsController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createChatRoom(@AuthenticationPrincipal User user,
                                            @RequestBody CreateChatRoomDTO createChatRoomDTO) {
        boolean isCreate = chatRoomService.createRoom(user, createChatRoomDTO);
        CustomResponse customResponse = new CustomResponse();
        if (isCreate) {
            customResponse.setCustom("Successful create chatroom");
        } else {
            customResponse.setCustom("You not create chatroom");
        }
        return ResponseEntity.ok(customResponse);
    }
}
