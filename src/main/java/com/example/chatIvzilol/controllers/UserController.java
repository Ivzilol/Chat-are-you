package com.example.chatIvzilol.controllers;

import com.example.chatIvzilol.service.UserService;
import com.example.chatIvzilol.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
