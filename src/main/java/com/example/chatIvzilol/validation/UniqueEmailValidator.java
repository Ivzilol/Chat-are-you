package com.example.chatIvzilol.validation;

import com.example.chatIvzilol.service.UserService;
import com.example.chatIvzilol.validation.annotation.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final UserService userService;

    public UniqueEmailValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return this.userService.findUserByEmail(email).isEmpty();
    }
}
