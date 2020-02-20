package com.arrnaux.demetria.core.userPost.model;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class SNPostDTO extends SNPost {
    private SNUser owner;

}
