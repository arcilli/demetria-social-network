package com.arrnaux.demetria.core.userPost.model;


import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.*;
import lombok.extern.log4j.Log4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)

@Document(collection = "post")

public class SNPost {
    @Id
    protected String id;

    protected SNUser owner;

    protected String content;

    @NotNull
    protected PostVisibility visibility;

    protected List<Vote> voteList;

    protected List<Comment> commentList;

    protected Date creationDate;

    public boolean appendComment(Comment comment) {
        if (null == commentList) {
            commentList = new ArrayList<>();
        }
        return commentList.add(comment);
    }
}