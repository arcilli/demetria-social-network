package com.arrnaux.demetria.core.userAccount.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// TODO: ADD validations
public class SNUserLoginDTO {

    @NotNull
    @NotEmpty
    private String email;

    @NotNull
    @NotEmpty

    // Uncomment this in production
    // @Length(min = 5, message = "*Your password must have at least 5 characters")
    private String password;
}