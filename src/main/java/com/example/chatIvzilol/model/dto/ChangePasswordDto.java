package com.example.chatIvzilol.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    private String oldPassword;

    private String newPassword;

    private String confirmNewPassword;
}
