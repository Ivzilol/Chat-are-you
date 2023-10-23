package com.example.chatIvzilol.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;
}
