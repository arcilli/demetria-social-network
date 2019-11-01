package com.arrnaux.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// TODO: ADD checkings
public class SNUserDTO {
    private String firstName;
    private String lastName;
    private String password;
    private String matchingPassword;

//    @ValidEmail
    // TODO: need to fix the problem with ValidEmail

    private String email;

    public SNUser toUser(){
        return new SNUser(-1, lastName,firstName, password, email);
    }
}
