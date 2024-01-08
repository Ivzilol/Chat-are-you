package com.example.chatIvzilol.model.dto;

import com.example.chatIvzilol.validation.annotation.UniqueEmail;
import com.example.chatIvzilol.validation.annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;



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
    private MultipartFile avatar;

    public UserRegistrationDTO(String username, String firstName, String lastName, String password, String confirmPassword, String email, MultipartFile avatar) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
        this.avatar = avatar;
    }

    public UserRegistrationDTO() {
    }
}
