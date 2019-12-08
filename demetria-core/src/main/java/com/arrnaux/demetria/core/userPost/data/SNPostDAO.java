package com.arrnaux.demetria.core.userPost.data;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.SNPost;

import java.util.List;

public interface SNPostDAO {
    // returns a list of the posts associated with an user
    List<SNPost> getUserPosts(SNUser snUser);

    //
    List<SNPost> getUserPostsDateDesc(SNUser snUser);

    // return the id of just created post
    SNPost savePost(SNPost snPost);

    boolean removePost(String postId);
}