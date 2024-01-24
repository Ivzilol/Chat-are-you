package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.model.dto.*;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.response.CustomResponse;
import com.example.chatIvzilol.service.UserService;
import com.example.chatIvzilol.util.JwtUtil;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static com.example.chatIvzilol.common.ConstantMessages.*;
import static com.example.chatIvzilol.common.ErrorMessages.INVALID_EMAIL;
import static com.example.chatIvzilol.common.ErrorMessages.INVALID_PASSWORD;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createUser(
            @RequestPart(value = "avatar", required = false) MultipartFile file,
            @RequestPart(value = "dto") UserRegistrationDTO userRegistrationDTO) throws MessagingException, IOException {
        this.userService.createUser(userRegistrationDTO, file);
        this.userService.sendVerificationEmail(userRegistrationDTO);
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            userRegistrationDTO.getUsername(), userRegistrationDTO.getPassword()
                    ));
            User user = (User) authentication.getPrincipal();
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtUtil.generateToken(user)
                    ).body(userRegistrationDTO);
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/register/verify/{verification}")
    private ResponseEntity<?> verificationUser(@PathVariable String verification) {
        User user = this.userService.validateUser(verification);
        if (!user.getEmail().isEmpty()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    @PostMapping("/register/forgotten-password")
    public ResponseEntity<?> forgottenPasswordEmail(@RequestBody ForgottenPasswordEmailDto forgottenPasswordDto) throws MessagingException, UnsupportedEncodingException {
        Optional<User> email = this.userService.findByEmail(forgottenPasswordDto.getEmail());
        if (email.isPresent()) {
            this.userService.sendEmailNewPassword(email);
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            CustomResponse customResponse = new CustomResponse();
            customResponse.setCustom(INVALID_EMAIL);
            return ResponseEntity.ok(customResponse);
        }
    }
    @PatchMapping("/register/forgotten-password/new-password")
    public ResponseEntity<?> forgottenPasswordNewPassword(@RequestBody ForgottenPasswordNewPasswordDto forgottenPasswordNewPasswordDto) {
        boolean newPassword = this.userService.forgottenPasswordSetNew(forgottenPasswordNewPasswordDto);
        if (newPassword) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        } else {
            CustomResponse customResponse = new CustomResponse();
            customResponse.setCustom(INVALID_PASSWORD);
            return ResponseEntity.ok(customResponse);
        }
    }
    @GetMapping("")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        Optional<UserDTO> currentUser = this.userService.getCurrentUser(user);
        return ResponseEntity.ok(currentUser);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        Optional<UserDTO> user = this.userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    @PatchMapping("/edit/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                        @RequestPart(value = "avatar", required = false) MultipartFile avatar,
                                        @RequestPart(value = "dto") UpdateUserDTO updateUserDTO) throws IOException {
        boolean updateUser = this.userService.updateUser(updateUserDTO, id, avatar);
        CustomResponse customResponse = new CustomResponse();
        customResponse.setCustom(updateUser ? SUCCESSFUL_UPDATE_USER : UNSUCCESSFUL_UPDATE_USER);
        return ResponseEntity.ok(customResponse);
    }
    @PatchMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto,
                                            @AuthenticationPrincipal User user) {
        boolean userChangePass = this.userService.changeUserPassword(changePasswordDto, user);
        if (userChangePass) {
            CustomResponse customResponse = new CustomResponse();
            customResponse.setCustom(SUCCESSFUL_CHANGE_PASSWORD);
            return ResponseEntity.ok(customResponse);
        } else {
            return ResponseEntity.ok(null);
        }
    }
}
