package com.arrnaux.userservice.userPost.model;

import lombok.*;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public enum VoteValue {
    UP,
    DOWN
}
