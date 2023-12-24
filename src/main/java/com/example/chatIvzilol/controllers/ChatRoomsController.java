package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.model.dto.AddUserRoomDTO;
import com.example.chatIvzilol.model.dto.CreateChatRoomDTO;
import com.example.chatIvzilol.model.dto.OtherUsersDTO;
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

    @GetMapping("/{roomId}")
    public ResponseEntity<?> getUsersChatRoom(@PathVariable String roomId) {
        Set<OtherUsersDTO> otherUsersDTO = this.chatRoomService.findUsersOnRoom(roomId);
        return ResponseEntity.ok(otherUsersDTO);
    }

    @PostMapping("/add-user/{roomId}")
    public ResponseEntity<?> addUserInRoom(@PathVariable String roomId,
                                           @AuthenticationPrincipal User user,
                                           @RequestBody AddUserRoomDTO addUserRoomDTO) {
        boolean isAdded = this.chatRoomService.addUserInRoom(roomId, user, addUserRoomDTO);
        CustomResponse customResponse = new CustomResponse();
        if (isAdded) {
            customResponse.setCustom("Successful add user in room");
        } else {
            customResponse.setCustom("Unsuccessful add user in room");
        }
        return ResponseEntity.ok(customResponse);
    }

    @DeleteMapping("/left/{roomId}")
    public ResponseEntity<?> removeUser(@PathVariable String roomId,
                                        @AuthenticationPrincipal User user) {
        boolean isRemove = this.chatRoomService.removeUserFromChatRoom(roomId, user);
        CustomResponse customResponse = new CustomResponse();
        if (isRemove) {
            customResponse.setCustom("Successful left room");
        } else {
            customResponse.setCustom("Unsuccessful left room");
        }
        return ResponseEntity.ok(customResponse);
    }
}
