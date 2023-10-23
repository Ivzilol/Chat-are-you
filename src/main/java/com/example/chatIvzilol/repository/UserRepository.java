package com.example.chatIvzilol.repository;

import com.example.chatIvzilol.model.dto.UserDTO;
import com.example.chatIvzilol.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User findByVerificationCode(String code);
    @Query("select u from User as u" +
            " where u.username = :username and u.isValidate = true")
    Optional<User> findByUsernameAndIsValidate(String username);
    @Query("select new com.example.chatIvzilol.model.dto.UserDTO(" +
            "u.id, u.username, u.firstName, u.lastName, u.email, u.avatar)" +
            " from User as u" +
            " where u.username = :username")
    UserDTO findCurrentUserByUsername(String username);

    @Query("select new com.example.chatIvzilol.model.dto.UserDTO(" +
            "u.id, u.username, u.firstName, u.lastName, u.email, u.avatar)" +
            " from User as u" +
            " where u.username = :username")
    Optional<UserDTO> findUserByUsername(String username);

    @Query("select new com.example.chatIvzilol.model.dto.UserDTO(" +
            "u.id, u.username, u.firstName, u.lastName, u.email, u.avatar)" +
            " from User as u" +
            " where u.id = :id")
    Optional<UserDTO> findUserById(Long id);
    @Query("select u from User as u" +
            " where u.id = :id")
    User findByUserId(Long id);
}
