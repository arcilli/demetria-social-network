package com.arrnaux.demetria.core.models.followRelation;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GraphPersonEntity {
    private String userName;
    private String storedId;

    public GraphPersonEntity(SNUser user) {
        if (null != user.getId() && null != user.getUserName()) {
            this.userName = user.getUserName();
            this.storedId = user.getId();
        }
    }
}