package com.arrnaux.userservice.userPost.data;

import com.arrnaux.userservice.userAccount.data.SequenceDAO;
import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userPost.model.SNPost;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Log4j
@Repository

public class SNPostDAODefault implements SNPostDAO {

    private static final String SNPostCollectionName = "sNPost";
    @Autowired
    private SNPostRepository snPostRepository;
    @Autowired
    private SequenceDAO sequenceDAO;

    @Override
    public List<SNPost> getUserPosts(SNUser snUser) {
//        log.info("Retrieve posts for user: " + snUser);
//        return snPostRepository.findByOwnerId(snUser.getId());
        return null;
    }

    @Override
    public boolean insertPost(SNPost snPost) {
        snPost.setId(sequenceDAO.getNextSequenceId(SNPostCollectionName));
        snPostRepository.save(snPost);
        return false;
    }
}
