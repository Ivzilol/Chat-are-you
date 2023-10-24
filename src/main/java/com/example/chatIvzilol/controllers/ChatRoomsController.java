package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.model.dto.CreateChatRoomDTO;
import com.example.chatIvzilol.model.dto.OtherUsersDTO;
import com.example.chatIvzilol.model.dto.UserDTO;
import com.example.chatIvzilol.model.dto.UserRoomsDTO;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.response.CustomResponse;
import com.example.chatIvzilol.service.ChatRoomService;
import com.example.chatIvzilol.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/chat-rooms")
@CrossOrigin(origins = {"http://localhost:3000/"}, allowCredentials = "false", allowedHeaders = "true")
public class ChatRoomsController {

    private final ChatRoomService chatRoomService;

    private final UserService userService;

    public ChatRoomsController(ChatRoomService chatRoomService, UserService userService) {
        this.chatRoomService = chatRoomService;
        this.userService = userService;
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

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal User user) {
        Set<OtherUsersDTO> allUsers = this.userService.getAllUser(user);
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/user-rooms")
    public ResponseEntity<?> getUserRooms(@AuthenticationPrincipal User user) {
        Set<UserRoomsDTO> userRoomsDTO = this.chatRoomService.getUserRooms(user);
        return ResponseEntity.ok(userRoomsDTO);
    }
}
