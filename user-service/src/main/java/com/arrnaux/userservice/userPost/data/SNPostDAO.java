package com.arrnaux.userservice.userPost.data;

import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userPost.model.SNPost;

import java.util.List;

public interface SNPostDAO {
    // returns a list of the posts associated with an user
    List<SNPost> getUserPosts(SNUser snUser);

    // return the id of just created post
//    long createPost(SNPost snPost);

}
