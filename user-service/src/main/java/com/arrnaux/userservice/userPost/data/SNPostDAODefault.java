package com.arrnaux.userservice.userPost.data;

import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userPost.model.SNPost;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Log4j
public class SNPostDAODefault implements SNPostDAO {

    @Autowired
    private SNPostRepository snPostRepository;

    @Override
    public List<SNPost> getUserPosts(SNUser snUser) {
//        log.info("Retrieve posts for user: " + snUser);
//        return snPostRepository.findByOwnerId(snUser.getId());
        return null;
    }

//    @Override
//    public long createPost(SNPost snPost) {
//        log.info("Creating post: " + snPost);
//        snPostRepository.insertSnPost(snPost);
//        // TODO: get created post id & return it
//        // TODO: else, throw an error
//        return -1;
//    }
}
