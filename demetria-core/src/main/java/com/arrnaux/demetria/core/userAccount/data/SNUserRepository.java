package com.arrnaux.demetria.core.userAccount.data;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SNUserRepository extends MongoRepository<SNUser, String> {
    SNUser findByFirstName(String firstName);

    List<SNUser> findByLastName(String lastName);

    // TODO: inspect this method on some null/nullable cases
    Optional<SNUser> findById(String id);

    Optional<SNUser> findByEmail(String email);

    Optional<SNUser> findByEmailAndPassword(String email, String password);
}