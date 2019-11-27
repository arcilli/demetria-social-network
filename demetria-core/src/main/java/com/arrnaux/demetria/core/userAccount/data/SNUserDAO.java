package com.arrnaux.demetria.core.userAccount.data;


import com.arrnaux.demetria.core.userAccount.model.SNUser;

public interface SNUserDAO {

    // gets an SNUser for a specific id
    SNUser getUser(String id);

    SNUser findUserByEmail(String email);

    SNUser findUserByEmailAndPassword(String email, String password);

    SNUser saveSNUser(SNUser snUser);
}