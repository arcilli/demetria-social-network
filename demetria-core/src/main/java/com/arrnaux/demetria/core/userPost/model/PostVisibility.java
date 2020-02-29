package com.arrnaux.demetria.core.userPost.model;

import lombok.Getter;

@Getter
//@ToString(callSuper = true)
public enum PostVisibility {
    PRIVATE("Private"),
    PUBLIC("Public"),
    // The none value is used only internal, this should not be dispalyed to user.
    NONE("");

    private final String displayValue;

    PostVisibility(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}