package com.arrnaux.demetria.core.userPost.data;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.PostVisibility;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import com.arrnaux.demetria.core.userPost.model.Vote;
import org.springframework.lang.Nullable;

import java.util.List;

public interface SNPostDAO {

    @Nullable
    SNPost getPostById(String postId);

    // returns a list of the posts associated with an user
    List<SNPost> getUserPosts(SNUser snUser);

    //
    List<SNPost> getUserPostsDateDesc(String id);

    List<SNPost> getUserPostsDescending(String userName, PostVisibility postVisibility);

    SNPost savePost(SNPost snPost);

    // returns the number of posts that will be deleted?
    int removePost(String postId);

    /**
     * @param vote The vote that will be removed.
     * @return The post that has the vote, but after removing the vote from the votes list.
     */
    @Nullable
    SNPost removeVote(Vote vote);
}