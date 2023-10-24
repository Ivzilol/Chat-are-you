package com.example.chatIvzilol.repository;

import com.example.chatIvzilol.model.dto.OtherUsersDTO;
import com.example.chatIvzilol.model.dto.UserRoomsDTO;
import com.example.chatIvzilol.model.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select new com.example.chatIvzilol.model.dto.OtherUsersDTO(" +
            " u.id, u.username)" +
            " from ChatRoom as cr" +
            " JOIN cr.userRooms as u" +
            " where cr.uniqueCode = :roomId")
    Set<OtherUsersDTO> findUsersInRoom(String roomId);

    @Query("select new com.example.chatIvzilol.model.dto.UserRoomsDTO(" +
            " cr.id, cr.name, cr.uniqueCode)" +
            " from ChatRoom as cr" +
            " JOIN cr.userRooms as u" +
            " where u.username = :username")
    Set<UserRoomsDTO> findUserRooms(String username);
}
