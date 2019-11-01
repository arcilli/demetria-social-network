package com.arrnaux.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SNUser {
    @Id
    private long id;

    private String lastName;
    private String firstName;
    private String password;
    private String email;

    @Override
    public String toString() {
        return "SNUser{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                '}';
    }

//    // TODO: find another solution, not to hard-code these values!
//    public SNUser(SNUserDTO userDTO) {
//        this.id = -1; //TODO: this value should be auto-generated
//        this.lastName = userDTO.getLastName();
//        this.firstName = userDTO.getFirstName();
//        this.password = userDTO.getPassword();
//        this.email = userDTO.getEmail();
//    }
}
