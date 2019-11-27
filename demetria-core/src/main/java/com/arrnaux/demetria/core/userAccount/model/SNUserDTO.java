package com.arrnaux.demetria.core.userAccount.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
// Should contain SNUser, but without Password
public class SNUserDTO extends SNUser {
    public SNUserDTO(SNUser snUser) {
        super();
        this.setPassword(null);
    }
}