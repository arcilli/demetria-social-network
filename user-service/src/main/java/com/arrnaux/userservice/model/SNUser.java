package com.arrnaux.userservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper=true)
// TODO: add fields validation
public class SNUser extends SNUserLoginDTO {

    // TODO: find a solution for autoincrement user id
    @Id
    protected static long id=5;
    protected String firstName;
    protected String lastName;

    public SNUser(long id, String firstName, String lastName, String email, String password) {
        super(email, password);
        this.id = id;
        this.id += 1;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public SNUser(SNUserRegistrationDTO snUserRegistrationDTO) {
        super(snUserRegistrationDTO.getEmail(), snUserRegistrationDTO.getPassword());
        this.firstName = snUserRegistrationDTO.getFirstName();
        this.lastName = snUserRegistrationDTO.getLastName();
    }
}
