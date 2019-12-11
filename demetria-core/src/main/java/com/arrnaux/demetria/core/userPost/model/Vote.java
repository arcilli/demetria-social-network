package com.arrnaux.demetria.core.userPost.model;

import lombok.*;
import lombok.extern.log4j.Log4j;

@Log4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class Vote {
    protected long id;

    protected long postId;

    protected long ownerId;

    protected VoteValue value;
}