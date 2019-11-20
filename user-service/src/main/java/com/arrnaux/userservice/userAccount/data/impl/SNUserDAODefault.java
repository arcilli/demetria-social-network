package com.arrnaux.userservice.userAccount.data.impl;

import com.arrnaux.userservice.userAccount.data.SNUserDAO;
import com.arrnaux.userservice.userAccount.data.SNUserRepository;
import com.arrnaux.userservice.userAccount.model.SNUser;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NoArgsConstructor
@Repository

public class SNUserDAODefault implements SNUserDAO {

    @Autowired
    private SNUserRepository snUserRepository;

    @Override
    public SNUser getUser(String id) {
        Optional<SNUser> user = snUserRepository.findById(id);
        // Throw an error
        // Log this, no user is found
        return user.orElse(null);
    }

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

    @Override
    public SNUser findUserByEmailAndPassword(String email, String password) {
        Optional<SNUser> snUser = snUserRepository.findByEmailAndPassword(email, password);
        return snUser.orElse(null);
    }

    @Override
    // TODO: add a case for false, when an error occurs
    public SNUser saveSNUser(SNUser snUser) {
        snUserRepository.save(snUser);
        return snUserRepository.findByEmail(snUser.getEmail()).orElse(null);
    }
}