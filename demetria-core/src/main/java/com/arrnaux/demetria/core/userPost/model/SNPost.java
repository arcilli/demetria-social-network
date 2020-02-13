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

    protected List<Vote> voteList = null;

    protected List<Comment> commentList = null;

    protected Date creationDate;

    protected Double averageRank = 0.0;

    public void appendComment(Comment comment) {
        if (null == commentList) {
            commentList = new ArrayList<>();
        }
        commentList.add(comment);
    }

    public void appendVote(Vote vote) {
        if (null == voteList) {
            voteList = new ArrayList<>();
        }
        voteList.add(vote);
    }

    public void computeAverageRank() {
        this.averageRank = this.voteList.stream().
                mapToDouble(Vote::getValue)
                .average().orElse(-1);
    }
}