package com.arrnaux.demetria.core.followRelation.model;

public enum FollowRelationValidity {
    INVALID(false),
    VALID(true);

    private final boolean value;

    FollowRelationValidity(Boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

}
