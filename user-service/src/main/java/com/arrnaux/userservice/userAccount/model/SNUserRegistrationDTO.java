package com.arrnaux.userservice.userAccount.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString(callSuper=true)
public class SNUserRegistrationDTO extends SNUser {

    @NotNull
    private String passwordMatch;
}
