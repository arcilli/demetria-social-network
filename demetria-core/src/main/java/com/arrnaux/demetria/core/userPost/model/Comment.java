package com.arrnaux.demetria.core.userPost.model;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private String id;

    private String ownerId;

    // This field will is not null only when the class is used to transfer data (as a DTO).
    private SNUser owner = null;

    private String content;

    private Date creationDate;
}
