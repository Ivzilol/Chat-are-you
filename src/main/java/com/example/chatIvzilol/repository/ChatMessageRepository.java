package com.example.chatIvzilol.repository;

import com.example.chatIvzilol.model.dto.ChatMessageDTO;
import com.example.chatIvzilol.model.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select new com.example.chatIvzilol.model.dto.ChatMessageDTO(" +
            "cm.id, cm.message, cm.createdDate, u.username)" +
            " from ChatMessage as cm" +
            " JOIN ChatRoom as cr on cm.chatRoom.id = cr.id" +
            " JOIN User as u on cm.messageCreator.id = u.id" +
            " where cr.uniqueCode = :room" +
            " order by cm.createdDate asc")
    Set<ChatMessageDTO> findByRoom(String room);
}
