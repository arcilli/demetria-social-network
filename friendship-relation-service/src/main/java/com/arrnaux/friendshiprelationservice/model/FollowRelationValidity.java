package com.arrnaux.friendshiprelationservice.model;

import lombok.Getter;

@Getter
public enum FollowRelationValidity {
    INVALID(false),
    VALID(true);

    private final boolean value;

    FollowRelationValidity(Boolean value) {
        this.value = value;
    }

}
