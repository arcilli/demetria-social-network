package com.arrnaux.demetria.core.userPost.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public enum VoteValue {
    UP,
    DOWN
}