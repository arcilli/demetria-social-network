package com.arrnaux.demetria.core.models.userPost;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Transient
    public static final String DEFAULT_POST_ID = "0";

    private String ownerId;

    // This field will is not null only when the class is used to transfer data (as a DTO).
    // The field will be stored in DB as null, never with a value.
    private SNUser owner = null;

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