package com.example.chatIvzilol.service;

import com.example.chatIvzilol.model.dto.AddUserRoomDTO;
import com.example.chatIvzilol.model.dto.CreateChatRoomDTO;
import com.example.chatIvzilol.model.dto.OtherUsersDTO;
import com.example.chatIvzilol.model.dto.UserRoomsDTO;
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
            chatRoom.setUserCreator(currentUser);
            Set<User> check = new HashSet<>();
            chatRoom.setUserRooms(check);
            chatRoom.getUserRooms().add(currentUser);
            this.chatRoomRepository.save(chatRoom);
            return true;
        }
        return false;
    }
    private static boolean isAdmin(User user) {
        return user.getAuthorities()
                .stream().anyMatch(auth -> AuthorityEnum.chatModerator.name().equals(auth.getAuthority()));
    }
    public Set<UserRoomsDTO> getUserRooms(User user) {
        return this.chatRoomRepository.findUserRooms(user.getUsername());
    }
    public Set<OtherUsersDTO> findUsersOnRoom(String roomId) {
        return this.chatRoomRepository.findUsersInRoom(roomId);
    }
    public boolean addUserInRoom(String roomId, User user, AddUserRoomDTO addUserRoomDTO) {
       User userWhoAdds = this.userRepository.findByUserId(user.getId());
       User userForAdd = this.userRepository.findByUserId(addUserRoomDTO.getId());
       if (roomId != null && userWhoAdds != null) {
           ChatRoom chatRoom = this.chatRoomRepository.findByUniqueCode(roomId);
           chatRoom.getUserRooms().add(userForAdd);
           this.chatRoomRepository.save(chatRoom);
           return true;
       }
       return false;
    }
    public boolean removeUserFromChatRoom(String roomId, User user) {
        User userForRemove = this.userRepository.findByUserId(user.getId());
        ChatRoom chatRoom = this.chatRoomRepository.findByUniqueCode(roomId);
        if (userForRemove != null && chatRoom != null) {
            chatRoom.getUserRooms().remove(userForRemove);
            this.chatRoomRepository.save(chatRoom);
            return true;
        }
        return false;
    }
}
