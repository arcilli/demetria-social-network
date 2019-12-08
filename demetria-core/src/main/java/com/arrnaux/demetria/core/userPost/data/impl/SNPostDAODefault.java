package com.arrnaux.demetria.core.userPost.data.impl;


import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.data.SNPostRepository;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Log4j
@Repository
public class SNPostDAODefault implements SNPostDAO {

    @Autowired
    private SNPostRepository snPostRepository;

    @Override
    public List<SNPost> getUserPosts(SNUser snUser) {
        log.info("Retrieve posts for user: " + snUser);
        return snPostRepository.findByOwnerId(snUser.getId());
    }

    @Override
    public SNPost savePost(SNPost snPost) {
        Date now = new Date();
        snPost.setCreationDate(now);
        return snPostRepository.save(snPost);
    }

    @Override
    public List<SNPost> getUserPostsDateDesc(SNUser snUser) {
        return snPostRepository.findByOwnerIdOrderByCreationDateDesc(snUser.getId());
    }

    private Sort orderByDateDesc() {
        return Sort.by(Sort.Direction.ASC, "creationDate");
    }
}