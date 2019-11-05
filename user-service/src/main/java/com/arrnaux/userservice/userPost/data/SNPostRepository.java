package com.arrnaux.userservice.userPost.data;

import com.arrnaux.userservice.userPost.model.SNPost;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SNPostRepository extends MongoRepository<SNPost, String> {

    List<SNPost> findByOwnerId(long ownerId);

}
