package com.arrnaux.demetria.core.userPost.data;

import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SNPostRepository extends MongoRepository<SNPost, String> {

    List<SNPost> findByOwnerId(String ownerId);

}