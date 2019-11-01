package com.arrnaux.userservice;

import com.arrnaux.userservice.model.SNUser;

public interface UserContext {

    // Get the currently logged in SNUser or null if there is no authenticated user
    SNUser getCurrentUser();

    // Sets the currently logged in SNUser
    void setCurrentUser(SNUser SNUser);
}
