package com.arrnaux.userservice.userAccount.data;

import com.arrnaux.userservice.userAccount.model.SNUser;

public interface SNUserDAO {

    // gets an SNUser for a specific id
    SNUser getUser(long id);

    SNUser findUserByEmail(String email);

    SNUser findUserByEmailAndPassword (String email, String password);

    boolean saveSNUser(SNUser snUser);
}