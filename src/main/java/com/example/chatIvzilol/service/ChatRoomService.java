package com.example.chatIvzilol.service;

import com.example.chatIvzilol.model.dto.CreateChatRoomDTO;
import com.example.chatIvzilol.model.entity.ChatRoom;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.model.enums.AuthorityEnum;
import com.example.chatIvzilol.repository.ChatRoomRepository;
import com.example.chatIvzilol.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository, UserService userService) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public boolean createRoom(User user, CreateChatRoomDTO createChatRoomDTO) {
        User currentUser = userRepository.findByUserId(user.getId());
        if (createChatRoomDTO.getChatroomName() != null) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setName(createChatRoomDTO.getChatroomName());
            String code = RandomString.make(32);
            chatRoom.setUniqueCode(code);
            Set<User> check = new HashSet<>();
            chatRoom.setUserRooms(check);
            chatRoom.getUserRooms().add(currentUser);
            this.chatRoomRepository.save(chatRoom);
            this.userService.addRoleModerator(currentUser);
            return true;
        }
        return false;
    }

    private static boolean isAdmin(User user) {
        return user.getAuthorities()
                .stream().anyMatch(auth -> AuthorityEnum.chatModerator.name().equals(auth.getAuthority()));
    }
}
