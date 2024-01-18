package com.example.chatIvzilol.service;

import com.example.chatIvzilol.model.dto.*;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Value("${admin_password}")
    private String adminPass;
    private static final String REGISTER_URL = "http://localhost:3000/register";
    private static final String FORGOT_PASSWORD_URL = "http://localhost:3000/forgotten-password/";
    private final AuthorityRepository authorityRepository;
    private final CustomPasswordEncoder encoder;
    private final JavaMailSender javaMailSender;
    private final CloudinaryService cloudinaryService;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, CustomPasswordEncoder encoder, JavaMailSender javaMailSender, CloudinaryService cloudinaryService, CustomPasswordEncoder customPasswordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.encoder = encoder;
        this.javaMailSender = javaMailSender;
        this.cloudinaryService = cloudinaryService;
        this.customPasswordEncoder = customPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }
    public Optional<User> findUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
    public Optional<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }
    public void createUser(UserRegistrationDTO userRegistrationDTO, MultipartFile file) throws IOException {
        if (userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())
                && userRegistrationDTO.getPassword().equals(adminPass)) {
            User newUser = createUserOrAdmin(userRegistrationDTO, file);
            Authority authority = new Authority();
            authority.setAuthority("admin");
            authority.setUser(newUser);
            this.authorityRepository.save(authority);
        } else if (userRegistrationDTO.getPassword().equals(userRegistrationDTO.getConfirmPassword())) {
            User newUser = createUserOrAdmin(userRegistrationDTO, file);
            Authority authority = new Authority();
            authority.setAuthority("user");
            authority.setUser(newUser);
            authorityRepository.save(authority);
        }
    }
    private User createUserOrAdmin(UserRegistrationDTO userRegistrationDTO, MultipartFile file) throws IOException {
        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setPassword(encoder.getPasswordEncoder().encode(userRegistrationDTO.getPassword()));
        user.setEmail(userRegistrationDTO.getEmail());
        user.setVerificationCode(RandomString.make(64));
        user.setAvatar(getAvatar(file));
        userRepository.save(user);
        return user;
    }
    private String getAvatar(MultipartFile file) throws IOException {
        String avatarUrl = "";
        if (file != null) {
            avatarUrl = this.cloudinaryService.uploadAvatar(file);
        }
        return avatarUrl;
    }
    public void sendVerificationEmail(UserRegistrationDTO userRegistrationDTO) throws MessagingException, UnsupportedEncodingException {
        Optional<User> user = this.userRepository.findByEmail(userRegistrationDTO.getEmail());
        String subject = "Successful Registration";
        String senderName = "Chat are you Team";
        String mailContent = "<h4>Dear " + userRegistrationDTO.getFirstName()
                + " " + userRegistrationDTO.getLastName() + ",</h4>";
        mailContent += "<p>Thank you for registration</p>";
        String verifyUrl = REGISTER_URL + "/verify/" + user.get().getVerificationCode();
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

    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public void sendEmailNewPassword(Optional<User> email) throws MessagingException, UnsupportedEncodingException {
        String subject = "Forgotten password";
        String senderName = "Chat are you Team";
        String mailContent = "<h4>Dear " + email.get().getFirstName()
                + " " + email.get().getLastName() + ",</h4>";
        mailContent += "<p>You have requested a generate new password.</p>";
        String verifyUrl = FORGOT_PASSWORD_URL + email.get().getVerificationCode();
        mailContent += "<p>Please click on the \" NEW PASSWORD\" link to generate new password.<p/>";
        mailContent += "<h3><a href=\"" + verifyUrl + "\">NEW PASSWORD</a></h3>";
        mailContent += "<p>Chat are you team<p/>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("ivailoali@gmail.com", senderName);
        helper.setTo(email.get().getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        javaMailSender.send(message);
    }

    public boolean forgottenPasswordSetNew(ForgottenPasswordNewPasswordDto forgottenPasswordNewPasswordDto) {
        User user = this.userRepository
                .findByVerificationCode(forgottenPasswordNewPasswordDto.getVerificationCode());
        if (forgottenPasswordNewPasswordDto.getPassword() == null || user == null ||
                !forgottenPasswordNewPasswordDto.getPassword()
                        .equals(forgottenPasswordNewPasswordDto.getConfirmPassword())) {
            return false;
        } else {
            String encode = encoder.getPasswordEncoder()
                    .encode(forgottenPasswordNewPasswordDto.getPassword());
            user.setPassword(encode);
            this.userRepository.save(user);
            return true;
        }
    }

    public Optional<UserDTO> getCurrentUser(User user) {
        return this.userRepository.findUserByUsername(user.getUsername());
    }

    public Optional<UserDTO> getUserById(Long id) {
        return this.userRepository.findUserById(id);
    }
    public boolean updateUser(UpdateUserDTO updateUserDTO, Long id, MultipartFile avatar) throws IOException {
        User updateUser = this.userRepository.findByUserId(id);
        Optional<User> userUsername = this.userRepository.findByUsername(updateUserDTO.getUsername());
        Optional<User> userEmail = this.userRepository.findByEmail(updateUserDTO.getEmail());
        // Checking whether the username and email with which he is trying to change them
        // are not taken by another user
        if (userUsername.isPresent() && !Objects.equals(userUsername.get().getId(), id)) {
            return false;
        } else {
            if (userEmail.isPresent() && !Objects.equals(id, userEmail.get().getId())) {
                return false;
            } else {
                updateUser.setPassword(updateUser.getPassword());
                updateUser.setUsername(updateUserDTO.getUsername());
                updateUser.setFirstName(updateUserDTO.getFirstName());
                updateUser.setLastName(updateUserDTO.getLastName());
                updateUser.setEmail(updateUserDTO.getEmail());
                if (avatar != null) {
                    updateUser.setAvatar(getAvatar(avatar));
                }
                this.userRepository.save(updateUser);
                return true;
            }
        }
    }
    public boolean changeUserPassword(ChangePasswordDto changePasswordDto, User user) {
        boolean passwordMatch = ifPasswordMatch(changePasswordDto, user);
        if (passwordMatch) {
            String encodedPassword = customPasswordEncoder
                    .getPasswordEncoder().encode(changePasswordDto.getNewPassword());
            user.setPassword(encodedPassword);
            this.userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
    private boolean ifPasswordMatch(ChangePasswordDto changePasswordDto, User user) {
        boolean matchesOldPassword = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        user.getUsername(), changePasswordDto.getOldPassword()
                )).isAuthenticated();
        boolean matchesNewPassword = changePasswordDto.getNewPassword()
                .equals(changePasswordDto.getConfirmNewPassword());
        return matchesOldPassword && matchesNewPassword;
    }
    public void addRoleModerator(User currentUser) {
        Authority authority = new Authority();
        authority.setAuthority("chatModerator");
        authority.setUser(currentUser);
        authorityRepository.save(authority);
    }
    public Set<OtherUsersDTO> getAllUser(User user) {
        return this.userRepository.getAllUsers(user.getUsername());
    }
}
