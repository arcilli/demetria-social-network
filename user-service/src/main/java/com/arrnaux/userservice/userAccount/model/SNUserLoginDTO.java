package com.arrnaux.userservice.userAccount.model;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// TODO: ADD validations
public class SNUserLoginDTO {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
