package com.arrnaux.userservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SNUser {
    @Id
    private long id;

    private String lastName;
    private String firstName;
    private String password;
    private String email;

//    // TODO: find another solution, not to hard-code these values!
//    public SNUser(SNUserDTO userDTO) {
//        this.id = -1; //TODO: this value should be auto-generated
//        this.lastName = userDTO.getLastName();
//        this.firstName = userDTO.getFirstName();
//        this.password = userDTO.getPassword();
//        this.email = userDTO.getEmail();
//    }
}
