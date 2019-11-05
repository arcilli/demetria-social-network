package com.arrnaux.userservice.userPost.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public enum PostVisibility {
    PRIVATE,
    ONLY_FRIENDS,
    PUBLIC
}
