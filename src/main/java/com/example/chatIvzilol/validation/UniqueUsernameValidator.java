package com.example.chatIvzilol.validation;

import com.example.chatIvzilol.service.UserService;
import com.example.chatIvzilol.validation.annotation.UniqueUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final UserService userService;
    public UniqueUsernameValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return this.userService.findUserByUsername(username).isEmpty();
    }
}
