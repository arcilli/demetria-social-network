package com.arrnaux.demetria.core.userPost.data;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.PostVisibility;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import com.arrnaux.demetria.core.userPost.model.Vote;
import org.bson.types.ObjectId;
import org.springframework.lang.Nullable;

import java.util.List;

public interface SNPostDAO {

    /**
     * @param postId
     * @return
     */
    @Nullable
    SNPost getPostById(String postId);

    /**
     * @param snUser
     * @return a list of the posts associated with an user
     */
    List<SNPost> getUserPosts(SNUser snUser);

    /**
     * @param id
     * @return
     */
    List<SNPost> getUserPostsDateDesc(String ownerId);

    /**
     * @param userName
     * @param postVisibility
     * @return
     */
    List<SNPost> getUserPostsDateDesc(String ownerId, PostVisibility postVisibility);

    /**
     * @param snPost
     * @return
     */
    SNPost savePost(SNPost snPost);

    /**
     * @param postId
     * @return the number of posts that will be deleted?
     */
    int removePost(String postId);

    /**
     * @param vote The vote that will be removed.
     * @return The post that has the vote, but after removing the vote from the votes list.
     */
    @Nullable
    SNPost removeVote(Vote vote);
}