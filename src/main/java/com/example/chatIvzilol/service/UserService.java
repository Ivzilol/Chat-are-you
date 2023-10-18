package com.example.chatIvzilol.service;

import com.example.chatIvzilol.model.dto.UserRegistrationDTO;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Value("${admin_password}")
    public String adminPass;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void createUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())
                && userRegistrationDTO.getPassword().equals(adminPass)) {
            User newUser = createUserOrAdmin(userRegistrationDTO);
        }
    }

    private User createUserOrAdmin(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setFirstName(user.getFirstName());
        return user;
    }
}
