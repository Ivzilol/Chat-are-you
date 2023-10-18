package com.example.chatIvzilol.service;

import com.example.chatIvzilol.model.dto.UserDTO;
import com.example.chatIvzilol.model.dto.UserRegistrationDTO;
import com.example.chatIvzilol.model.entity.Authority;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.repository.AuthorityRepository;
import com.example.chatIvzilol.repository.UserRepository;
import com.example.chatIvzilol.util.CustomPasswordEncoder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    @Value("${admin_password}")
    private String adminPass;

    private final AuthorityRepository authorityRepository;

    private final CustomPasswordEncoder encoder;

    private final JavaMailSender javaMailSender;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, CustomPasswordEncoder encoder, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
        this.javaMailSender = javaMailSender;
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

    public void sendVerificationEmail(UserRegistrationDTO userRegistrationDTO) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = this.userRepository.findByEmail(userRegistrationDTO.getEmail());
        String siteUrl = "http://localhost:3000/register";
        String subject = "Successful Registration";
        String senderName = "Chat are you Team";
        String mailContent = "<h4>Dear " + userRegistrationDTO.getFirstName()
                + " " + userRegistrationDTO.getLastName() + ",</h4>";
        mailContent += "<p>Thank you for registration</p>";
        String verifyUrl = siteUrl + "/verify/" + user.get().getVerificationCode();
        mailContent += "<p>Please click on the \"ACTIVATE\" link to activate your account.<p/>";
        mailContent += "<h3><a href=\"" + verifyUrl + "\">ACTIVATE</a></h3>";
        mailContent += "<p>Chat are you team<p/>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("ivailoali@gmail.com", senderName);
        helper.setTo(userRegistrationDTO.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        javaMailSender.send(message);
    }

    public User validateUser(String verification) {
        User user = this.userRepository.findByVerificationCode(verification);
        if (!user.getEmail().isEmpty()) {
            user.setValidate(true);
            this.userRepository.save(user);
        }
        return user;
    }

    public Optional<User> validate(String username) {
        return this.userRepository.findByUsernameAndIsValidate(username);
    }

    public UserDTO findCurrentUser(String username) {
        return this.userRepository.findCurrentUserByUsername(username);
    }
}
