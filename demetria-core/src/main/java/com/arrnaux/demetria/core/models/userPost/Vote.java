package com.arrnaux.demetria.core.models.userPost;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)

public class Vote {
    @NotNull
    private String postId;

    private String ownerId;

    // This field will is not null only when the class is used to transfer data (as a DTO).
    private SNUser owner = null;

    @NotNull
    private long value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return value == vote.value &&
                postId.equals(vote.postId) &&
                owner.equals(vote.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, owner, value);
    }
}