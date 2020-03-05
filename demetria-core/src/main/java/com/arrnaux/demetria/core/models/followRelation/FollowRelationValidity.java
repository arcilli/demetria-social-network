package com.arrnaux.demetria.core.models.followRelation;

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
