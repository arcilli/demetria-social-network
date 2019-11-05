package com.arrnaux.userservice.userPost.data;

import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userPost.model.SNPost;

import java.util.List;

public interface SNPostDAO {
    List<SNPost> getUserPosts(SNUser snUser);

    List<SNPost> getPostById(long postId);

    long createPost(SNPost snPost);
}
