package com.arrnaux.demetria.core.models.userAccount;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class SNUserLoginDTO {

    @NotNull
    private String email;
    
    @NotNull
    private String password;
}