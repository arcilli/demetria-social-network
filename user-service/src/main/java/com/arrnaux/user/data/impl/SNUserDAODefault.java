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
    private SNUserRepository snUserRepository;

    private MongoOperations mongoOperations;

    @Autowired
    public SNUserDAODefault(SNUserRepository snUserRepository, MongoOperations mongoOperations) {
        this.snUserRepository = snUserRepository;
        this.mongoOperations = mongoOperations;
    }

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
    public SNUser findUserByEmailAndPlainPassword(@Nullable String email, @Nullable String plainPassword) {
        if (null == email || null == plainPassword) {
            return null;
        }
        Optional<String> hashedPassword = PasswordUtils.hashPassword(plainPassword, null);
        return hashedPassword.map(s -> findUserByEmailAndPassword(email, s)).orElse(null);
    }

    /**
     * Remove the user from document-oriented DB.
     * For a completely remove of the user from network, make sure it's also deleted from graph.
     *
     * @param email
     * @return
     */
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
            // Use matchedCount since an user can change its photo with the actual one.
            if (1 == updateResult.getMatchedCount()) {
                return findById(snUser.getId());
            }
        }
        return null;
    }

    @Override
    @Nullable
    public List<SNUser> findUserByInsensitiveQuery(String[] queryTerms) {
        if (null != queryTerms) {
            String regex = getRegexFromQueryTerms(queryTerms);
            Query query = new Query();
            query
                    .addCriteria(Criteria.where("lastName").regex(regex, "i"))
                    .addCriteria(Criteria.where("firstName").regex(regex, "i"))
                    .fields().exclude("password");
            return mongoOperations.find(query, SNUser.class);
        }
        return null;
    }

    @Override
    @Nullable
    public List<SNUser> findUserByPartialInsensitiveQuery(String[] queryTerms) {
        if (null != queryTerms) {
            String regex = getRegexFromQueryTerms(queryTerms);
            Criteria criteria = new Criteria();
            // i parameter is for case-insensitive match.
            criteria.orOperator(
                    Criteria.where("lastName").regex(regex, "i"),
                    Criteria.where("firstName").regex(regex, "i")
            );
            Query query = new Query(criteria);
            query.fields().exclude("password");
            return mongoOperations.find(query, SNUser.class);
        }
        return null;
    }

    @Nullable
    @Override
    public SNUser updateUserPassword(SNUser snUser, String newPlainPassword) {
        Optional<String> hashedPasswordOpt = PasswordUtils.hashPassword(newPlainPassword, null);
        if (!hashedPasswordOpt.isPresent() || null == snUser.getEmail()) {
            return null;
        }
        String hashedPassword = hashedPasswordOpt.get();
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(snUser.getEmail()));
        UpdateResult updateResult = mongoOperations.updateMulti(
                query,
                Update.update("password", hashedPassword),
                SNUser.class
        );
        // Use matched count since the new password can be equal to the oldest one.
        if (1 == updateResult.getMatchedCount()) {
            return findUserByEmail(snUser.getEmail());
        }
        return null;
    }

    private String getRegexFromQueryTerms(String[] queryTerms) {
        StringBuilder regexBuilder = new StringBuilder();
        if (1 != queryTerms.length) {
            for (int i = 0; i < queryTerms.length - 1; ++i) {
                regexBuilder.append(queryTerms[i]).append("|");
            }
        }
        regexBuilder.append(queryTerms[queryTerms.length - 1]);
        return regexBuilder.toString();
    }

}