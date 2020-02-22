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
    public SNUser findById(String id) {
        return snUserRepository.findById(id).orElse(null);
    }

    @Nullable
    @Override
    public SNUser findUserByEmail(String email) throws IllegalArgumentException {
        return snUserRepository.findByEmail(email).orElse(null);
    }

    @Nullable
    @Override
    public SNUser findUserByEmailAndPassword(String email, String password) {
        return snUserRepository.findByEmailAndPassword(email, password).orElse(null);
    }

    @Nullable
    @Override
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
    @Nullable
    public SNUser findUserByUsername(String username) {
        return snUserRepository.findByUserName(username).orElse(null);
    }
}