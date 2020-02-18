package com.arrnaux.demetria.core.userPost.data;

import com.arrnaux.demetria.core.userPost.model.PostVisibility;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface SNPostRepository extends MongoRepository<SNPost, String> {
    Optional<SNPost> findById(ObjectId id);

    Optional<List<SNPost>> findByOwnerId(ObjectId ownerId);

    // TODO: check if is any problem for this
    SNPost save(SNPost snPost);

    Optional<List<SNPost>> findByOwnerIdOrderByCreationDateDesc(ObjectId owner_id);

    Optional<List<SNPost>> findByUsernameAndVisibilityOrderByCreationDate(String userName, @NotNull PostVisibility visibility);

    Optional<List<SNPost>> deleteSNPostById(ObjectId id);
}