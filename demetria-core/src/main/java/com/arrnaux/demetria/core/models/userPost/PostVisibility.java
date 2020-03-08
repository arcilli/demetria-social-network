package com.arrnaux.demetria.core.models.userPost;

import lombok.Getter;

@Getter
public enum PostVisibility {
    PRIVATE("Private"),
    PUBLIC("Public"),
    // The none value is used only internal, this should not be dispalyed to user.
    NONE("");

    private final String displayValue;

    PostVisibility(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * @return the options for post visibility, in reversed order
     * Used in Thymleaf to show the public option first.
     */
    public static PostVisibility[] reversedValues() {
        int noOptions = PostVisibility.values().length;
        PostVisibility[] values = new PostVisibility[noOptions];
        for (int i = 0; i < noOptions; ++i) {
            values[i] = PostVisibility.values()[noOptions - 1 - i];
        }
        return values;
    }
}