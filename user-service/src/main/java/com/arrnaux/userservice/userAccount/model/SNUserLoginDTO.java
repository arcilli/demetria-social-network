package com.arrnaux.userservice.userAccount.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// TODO: ADD validations
public class SNUserLoginDTO {
    private String email;
    private String password;
}
