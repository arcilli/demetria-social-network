package com.arrnaux.demetria.core.userAccount.data.impl;

import com.arrnaux.demetria.core.userAccount.data.SNUserDAO;
import com.arrnaux.demetria.core.userAccount.data.SNUserRepository;
import com.arrnaux.demetria.core.userAccount.model.PasswordUtils;
import com.arrnaux.demetria.core.userAccount.model.SNUser;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NoArgsConstructor
@Repository

public class SNUserDAODefault implements SNUserDAO {

    @Autowired
    private SNUserRepository snUserRepository;

    @Nullable
    @Override
    public SNUser getUser(String id) {
        Optional<SNUser> user = snUserRepository.findById(id);
        // Throw an error
        // Log this, no user is found
        return user.orElse(null);
    }

    @Nullable
    @Override
    // throws IllegalArgumentException when email is null
    public SNUser findUserByEmail(String email) throws IllegalArgumentException {
        if (null == email) {
            throw new IllegalArgumentException("The email cannot be null");
        }
        // Throw an error
        // Log this if no user is found
        Optional<SNUser> snUser = snUserRepository.findByEmail(email);

        return snUser.orElse(null);
    }

    @Nullable
    @Override
    public SNUser findUserByEmailAndPassword(String email, String password) {
        Optional<SNUser> snUser = snUserRepository.findByEmailAndPassword(email, password);
        return snUser.orElse(null);
    }

    @Nullable
    @Override
    // TODO: add a case for false, when an error occurs
    public SNUser saveSNUser(SNUser snUser) {
        snUserRepository.save(snUser);
        return snUserRepository.findByEmail(snUser.getEmail()).orElse(null);
    }

    @Nullable
    @Override
    public SNUser findUserByEmailAndPlainPassword(String email, String plainPassword) {
        Optional<String> hashedPassword = PasswordUtils.hashPassword(plainPassword, null);
        return hashedPassword.map(s -> findUserByEmailAndPassword(email, s)).orElse(null);
    }

    @Override
    public Boolean removeUserAccount(String email) {
        try {
            snUserRepository.removeSNUserByEmail(email);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public SNUser findUserByUsername(String username) {
        Optional<SNUser> snUser = snUserRepository.findByUserName(username);
        return snUser.orElse(null);
    }
}