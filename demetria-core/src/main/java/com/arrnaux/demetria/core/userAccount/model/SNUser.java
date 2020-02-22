package com.arrnaux.demetria.core.userAccount.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@AllArgsConstructor

// TODO: add fields validation
@Document(collection = "user")
public class SNUser extends SNUserLoginDTO {

    @Id
    protected String id;

    @NotNull
    protected String firstName;

    @NotNull
    protected String lastName;

    @NotNull
    protected String userName;

    public SNUser(SNUserRegistrationDTO snUserRegistrationDTO) {
        super(snUserRegistrationDTO.getEmail(), snUserRegistrationDTO.getPassword());
        this.firstName = snUserRegistrationDTO.getFirstName();
        this.lastName = snUserRegistrationDTO.getLastName();
        this.userName = snUserRegistrationDTO.getUserName();
    }

    public void updateObjectWithNotNullValues(SNUser modifiedUser) {
        if (null != modifiedUser.getLastName()) this.setLastName(modifiedUser.getLastName());
        if (null != modifiedUser.getFirstName()) this.setFirstName(modifiedUser.getFirstName());
    }

    public void obfuscateUserInformation() {
        this.setPassword("");
        this.setEmail("");
    }
}