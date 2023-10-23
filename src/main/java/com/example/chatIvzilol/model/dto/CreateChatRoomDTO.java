package com.example.chatIvzilol.model.dto;

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
public class CreateChatRoomDTO {

    @Size(min = 3, max = 20)
    @NotNull
    private String chatroomName;
}


