package com.arrnaux.userservice.userAccount.data;

import com.arrnaux.userservice.userAccount.model.SNUser;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@NoArgsConstructor
public class SNUserDAODefault implements SNUserDAO {

    @Autowired
    private SNUserRepository snUserRepository;

    @Override
    public SNUser getUser(long id) {
        Optional<SNUser> user = snUserRepository.findById(id);
        // Throw an error
        // Log this, no user is found
        return user.get();
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

        // TODO: use lamda?
        if (snUser.isPresent()) {
            return snUser.get();
        }
        return null;
    }

    @Override
    public SNUser findUserByEmailAndPassword(String email, String password) {
        Optional<SNUser> snUser = snUserRepository.findByEmailAndPassword(email, password);
        if (snUser.isPresent()) {
            return snUser.get();
        }
        return null;
    }

    @Override
    // TODO: add a case for false, when an error occurs
    public boolean saveSNUser(SNUser snUser) {
        snUserRepository.save(snUser);
        return true;
    }
}
