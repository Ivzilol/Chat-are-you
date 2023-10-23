package com.example.chatIvzilol.model.dto;

import com.example.chatIvzilol.validation.annotation.UniqueEmail;
import com.example.chatIvzilol.validation.annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {

    @Size(min = 3, max = 20)
    @UniqueUsername
    private String username;


    private String firstName;


    private String lastName;

    @UniqueEmail
    @Email
    private String email;
}
