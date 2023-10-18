package com.example.chatIvzilol.service;

import com.example.chatIvzilol.model.dto.UserRegistrationDTO;
import com.example.chatIvzilol.model.entity.Authority;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.repository.AuthorityRepository;
import com.example.chatIvzilol.repository.UserRepository;
import com.example.chatIvzilol.util.CustomPasswordEncoder;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Value("${admin_password}")
    private String adminPass;

    private final AuthorityRepository authorityRepository;

    private final CustomPasswordEncoder encoder;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, CustomPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
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
            Authority authority = new Authority();
            authority.setAuthority("admin");
            authority.setUser(newUser);
            this.authorityRepository.save(authority);
        } else if (userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())) {
            User newUser = createUserOrAdmin(userRegistrationDTO);
            Authority authority = new Authority();
            authority.setAuthority("user");
            authority.setUser(newUser);
            authorityRepository.save(authority);
        }
    }

    private User createUserOrAdmin(UserRegistrationDTO userRegistrationDTO) {
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setPassword(encoder.getPasswordEncoder().encode(userRegistrationDTO.getPassword()));
        user.setEmail(userRegistrationDTO.getEmail());
        user.setVerificationCode(RandomString.make(64));
        userRepository.save(user);
        return user;
    }
}
