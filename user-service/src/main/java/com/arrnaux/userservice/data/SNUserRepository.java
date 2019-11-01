package com.arrnaux.userservice.data;

import com.arrnaux.userservice.model.SNUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface SNUserRepository extends MongoRepository<SNUser, String> {
    public SNUser findByFirstName(String firstName);

    public List<SNUser> findByLastName(String lastName);

    public Optional<SNUser> findById(long id);

    public Optional<SNUser> findByEmail(String email);

    public Optional<SNUser> findByEmailAndPassword(String email, String password);
}
