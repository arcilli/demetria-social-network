package com.arrnaux.demetria.core.userAccount.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Document(collection = "friends")
public class SNFriendshipRelation {

    SNUser[] friends = new SNUser[2];

}
