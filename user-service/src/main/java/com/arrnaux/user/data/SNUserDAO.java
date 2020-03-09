package com.arrnaux.user.data;


import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.lang.Nullable;

public interface SNUserDAO {

    @Nullable
    SNUser findById(String id);

    @Nullable
    SNUser findUserByEmail(String email);

    @Nullable
    SNUser findUserByEmailAndPassword(String email, String password);

    @Nullable
    SNUser saveSNUser(SNUser snUser);

    @Nullable
    SNUser findUserByEmailAndPlainPassword(String email, String plainPassword);

    Boolean removeUserAccount(String email);

    @Nullable
    SNUser findUserByUsername(String userName);
}