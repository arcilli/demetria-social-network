package com.arrnaux.demetria.core.userAccount.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    protected String firstName;

    protected String lastName;

    protected String userName;

    public SNUser(SNUserRegistrationDTO snUserRegistrationDTO) {
        super(snUserRegistrationDTO.getEmail(), snUserRegistrationDTO.getPassword());
        this.firstName = snUserRegistrationDTO.getFirstName();
        this.lastName = snUserRegistrationDTO.getLastName();
    }

    public SNUser modifyPartialFieldsFromObject(SNUser user) {
        if (user.getId() != null) this.setId(user.getId());

        if (user.getEmail() != null) this.setEmail(user.getEmail());

        if (user.getPassword() != null) this.setPassword(user.getPassword());

        if (user.getFirstName() != null) this.setFirstName(user.getFirstName());

        if (user.getLastName() != null) this.setLastName(user.getLastName());

        return this;
    }
}