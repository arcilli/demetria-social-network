package com.arrnaux.demetria.core.userPost.model;

import lombok.*;
import lombok.extern.log4j.Log4j;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Log4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Vote {
    @NotNull
    protected String postId;
    @NotNull
    protected String ownerId;
    @NotNull
    protected long value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return value == vote.value &&
                postId.equals(vote.postId) &&
                ownerId.equals(vote.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, ownerId, value);
    }
}