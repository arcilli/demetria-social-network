package com.arrnaux.userservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString(callSuper=true)
// Should contain SNUser, but without Password
public class SNUserDTO extends SNUser {

}
