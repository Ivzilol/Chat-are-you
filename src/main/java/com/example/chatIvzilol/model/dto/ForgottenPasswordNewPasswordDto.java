package com.example.chatIvzilol.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ForgottenPasswordNewPasswordDto {

    private String verificationCode;

    private String password;

    private String confirmPassword;
}
