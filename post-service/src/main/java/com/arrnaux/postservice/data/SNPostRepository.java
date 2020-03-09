package com.arrnaux.postservice.data;

import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface SNPostRepository extends MongoRepository<SNPost, String> {
    Optional<List<SNPost>> findByOwnerId(String ownerId);

    // TODO: check if is any problem for this
    SNPost save(SNPost snPost);

    Optional<List<SNPost>> findByOwnerIdOrderByCreationDateDesc(String ownerId);

    Optional<List<SNPost>> findByOwnerIdAndVisibilityOrderByCreationDate(String owner_id,
                                                                         @NotNull PostVisibility visibility);

    Optional<List<SNPost>> deleteSNPostById(String id);

    Optional<SNPost> findFirstByOwnerIdInAndVisibilityOrderByCreationDateDesc(List<String> ownerId,
                                                                              PostVisibility visibility);

    Optional<SNPost> findFirstByOwnerIdInOrderByCreationDateDesc(List<String> ownerId);
}