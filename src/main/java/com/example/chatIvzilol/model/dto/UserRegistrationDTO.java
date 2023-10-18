package com.example.chatIvzilol.model.dto;

import com.example.chatIvzilol.validation.annotation.UniqueEmail;
import com.example.chatIvzilol.validation.annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationDTO {

    @UniqueUsername
    @NotNull
    @Size(min = 3, max = 20)
    private String username;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String password;

    @NotNull
    private String confirmPassword;

    @UniqueEmail
    @Email
    @NotNull
    private String email;
}
