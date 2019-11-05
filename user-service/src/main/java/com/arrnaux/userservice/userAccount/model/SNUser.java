package com.arrnaux.userservice.userAccount.model;

import com.arrnaux.userservice.userPost.data.SNPostDAO;
import com.arrnaux.userservice.userPost.model.SNPost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
// TODO: add fields validation
public class SNUser extends SNUserLoginDTO {

    @Autowired
    SNPostDAO snPostDAO;
    // TODO: find a solution for autoincrement user id
    @Id
    protected static long id = 5;
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

    // TODO: decide on return type of the method
    public SNPost addPost(SNPost snPost) {
        // TODO: to be implemented
        // use injected snPostDAO
        return null;
    }
}
