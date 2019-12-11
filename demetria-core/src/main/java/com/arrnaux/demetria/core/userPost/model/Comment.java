package com.arrnaux.demetria.core.userPost.model;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comment")
public class Comment {

    @Id
    private String id;

    private SNUser owner;

    private String content;

    private Date creationDate;
}
