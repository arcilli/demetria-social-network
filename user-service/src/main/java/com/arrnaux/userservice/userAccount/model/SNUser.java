package com.arrnaux.userservice.userAccount.model;

import com.arrnaux.userservice.userPost.data.SNPostDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
// TODO: add fields validation

//@Document(collection = "user")
public class SNUser extends SNUserLoginDTO {
    // TODO: find a solution for autoincrement user id
    @Id
    protected long id;
    private int seq;
    protected String firstName;
    protected String lastName;
    @Autowired
    SNPostDAO snPostDAO;

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
