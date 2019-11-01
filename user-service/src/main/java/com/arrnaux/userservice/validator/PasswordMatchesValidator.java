package com.arrnaux.userservice.validator;

import com.arrnaux.userservice.model.SNUserDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        SNUserDTO user = (SNUserDTO) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}