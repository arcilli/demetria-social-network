package com.arrnaux.userservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SNUserRegistrationDTO extends SNUser {

    private String passwordMatch;
}
