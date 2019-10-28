package com.arrnaux.userservice.model;

import com.arrnaux.userservice.validator.PasswordMatches;
import com.arrnaux.userservice.validator.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches
public class UserDTO {
    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;

//    @ValidEmail
    // TODO: need to fix the problem with ValidEmail
    @NotNull
    @NotEmpty
    private String email;

    public User toUser(){
        return new User(-1, lastName,firstName, password, email);
    }
}
