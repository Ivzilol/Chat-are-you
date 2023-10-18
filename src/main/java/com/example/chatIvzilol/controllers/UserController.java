package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.model.dto.UserRegistrationDTO;
import com.example.chatIvzilol.model.entity.User;
import com.example.chatIvzilol.service.UserService;
import com.example.chatIvzilol.util.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000/"}, allowCredentials = "false", allowedHeaders = "true")
public class UserController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDTO) throws MessagingException, UnsupportedEncodingException {
        this.userService.createUser(userRegistrationDTO);
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
}
