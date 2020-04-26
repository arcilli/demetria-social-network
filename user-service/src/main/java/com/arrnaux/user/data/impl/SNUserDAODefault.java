package com.arrnaux.user.data.impl;

import com.arrnaux.demetria.core.models.userAccount.PasswordUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.user.data.SNUserDAO;
import com.arrnaux.user.data.SNUserRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Repository

public class SNUserDAODefault implements SNUserDAO {

    @Autowired
    private SNUserRepository snUserRepository;

    @Autowired
    private MongoOperations mongoOperations;

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

    @Override
    @Nullable
    public List<SNUser> findUserByNameQuery(String[] namesTerms) {
        Query query = new Query()
                .addCriteria(Criteria.where("lastName").in(Arrays.asList(namesTerms)))
                .addCriteria(Criteria.where("firstName").in(Arrays.asList(namesTerms)));
        return mongoOperations.find(query, SNUser.class);
    }

    @Override
    @Nullable
    public SNUser replaceProfileImage(SNUser snUser, String encodedProfileImage) {
        if (null != snUser.getId()) {
            Update update = new Update();
            update.set("profileImageBase64", encodedProfileImage);

            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(snUser.getId()));

            UpdateResult updateResult = mongoOperations.updateFirst(query, update, SNUser.class);
            if (1 == updateResult.getModifiedCount()) {
                return findById(snUser.getId());
            }
        }
        return null;
    }
}