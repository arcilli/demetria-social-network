package com.arrnaux.demetria.core.userAccount.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
// TODO: add fields validation

@Document(collection = "user")
public class SNUser extends SNUserLoginDTO {
    // TODO: find a solution for autoincrement user id
    @Id
    protected String id;

    @NotEmpty
    protected String firstName;

    @NotEmpty
    protected String lastName;

    // Don't know if this is used somewhere
    // @Autowired
    //SNPostDAO snPostDAO;

    public SNUser(String id, String firstName, String lastName, String email, String password) {
        super(email, password);
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

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