package com.arrnaux.userservice.data;

import com.arrnaux.userservice.model.SNUser;

public interface SNUserDAO {

    // gets an SNUser for a specific id
    SNUser getUser(long id);

    SNUser findUserByEmail(String email);

    SNUser findUserByEmailAndPassword (String email, String password);
}