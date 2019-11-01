package com.arrnaux.userservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// TODO: ADD validations
public class SNUserDTO {
    private String password;
    private String email;

    public SNUser toUser() {
        SNUser user = new SNUser();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }
}
