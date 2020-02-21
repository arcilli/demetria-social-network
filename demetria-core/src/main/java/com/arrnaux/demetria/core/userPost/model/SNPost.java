package com.arrnaux.demetria.core.userPost.model;

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
@Builder

@Getter
@Setter
@ToString(callSuper = true)
@Document(collection = "post")
public class SNPost {
    @Id
    private String id;

    private String ownerId;

    private String content;

    @NotNull
    private PostVisibility visibility;

    private List<Vote> voteList = null;

    private List<Comment> commentList = null;

    private Date creationDate;

    private Double averageRank = 0.0;

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