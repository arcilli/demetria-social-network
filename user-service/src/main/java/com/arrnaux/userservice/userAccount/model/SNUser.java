package com.arrnaux.userservice.userAccount.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
// TODO: add fields validation

@Document(collection = "user")
public class SNUser extends SNUserLoginDTO {
    // TODO: find a solution for autoincrement user id
    @Id
    protected long id;
    protected String firstName;
    protected String lastName;

    // Don't know if this is used somewhere
    // @Autowired
    //SNPostDAO snPostDAO;

    public SNUser(long id, String firstName, String lastName, String email, String password) {
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
}
