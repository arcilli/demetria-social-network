package com.arrnaux.demetria.core.models.userAccount;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SNUserRegistrationDTO extends SNUser {

    private String passwordMatch;

    public boolean registrationFormIncomplete() {
        return
                this.getEmail() == null ||
                        this.getPassword() == null ||
                        this.getPasswordMatch() == null ||
                        this.getFirstName() == null ||
                        this.getUserName() == null ||
                        this.getLastName() == null;
    }

    public boolean passwordIsMatched() {
        return this.getPassword().equals(this.passwordMatch);
    }
}