package com.arrnaux.userservice.userPost.data;

import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userPost.model.SNPost;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SNPostDAODefault implements SNPostDAO {

    @Autowired
    private SNPostRepository snPostRepository;

    @Override
    public List<SNPost> getUserPosts(SNUser snUser) {
        return null;
    }

    @Override
    public List<SNPost> getPostById(long postId) {
        return null;
    }

    @Override
    public long createPost(SNPost snPost){
        return -1;
    }
}
