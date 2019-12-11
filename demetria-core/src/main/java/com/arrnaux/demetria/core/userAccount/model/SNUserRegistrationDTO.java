package com.arrnaux.demetria.core.userAccount.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SNUserRegistrationDTO extends SNUser {

    private String passwordMatch;
}