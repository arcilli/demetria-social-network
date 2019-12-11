package com.arrnaux.demetria.core.userAccount.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class SNUserLoginDTO {

    private String email;

    private String password;
}