package com.arrnaux.demetria.core.userPost.model;

import lombok.Getter;
import lombok.ToString;

@Getter
//@ToString(callSuper = true)
public enum PostVisibility {
    PRIVATE("Private"),
    ONLY_FRIENDS("Only friends"),
    PUBLIC("Public");

    private final String displayValue;

    PostVisibility(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}