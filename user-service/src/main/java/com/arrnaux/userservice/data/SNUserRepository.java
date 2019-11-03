package com.arrnaux.userservice.data;

import com.arrnaux.userservice.model.SNUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SNUserRepository extends MongoRepository<SNUser, String> {
    SNUser findByFirstName(String firstName);

    List<SNUser> findByLastName(String lastName);

    Optional<SNUser> findById(long id);

    Optional<SNUser> findByEmail(String email);

    Optional<SNUser> findByEmailAndPassword(String email, String password);
}
