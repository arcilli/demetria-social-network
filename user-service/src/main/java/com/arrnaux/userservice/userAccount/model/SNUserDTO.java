package com.arrnaux.userservice.userAccount.model;

import lombok.*;

@Getter
@Setter
@ToString(callSuper=true)
// Should contain SNUser, but without Password
public class SNUserDTO extends SNUser {

}
