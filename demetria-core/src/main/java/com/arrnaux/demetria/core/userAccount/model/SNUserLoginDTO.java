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

    private String email;

    private String password;
}