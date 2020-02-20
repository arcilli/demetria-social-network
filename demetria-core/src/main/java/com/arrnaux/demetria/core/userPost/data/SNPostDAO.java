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
     *
     * @param postId
     * @return
     */
    @Nullable
    SNPost getPostById(ObjectId postId);

    /**
     * @param snUser
     * @return a list of the posts associated with an user
     */
    List<SNPost> getUserPosts(SNUser snUser);

    /**
     *
     * @param id
     * @return
     */
    List<SNPost> getUserPostsDateDesc(ObjectId id);

    /**
     *
     * @param userName
     * @param postVisibility
     * @return
     */
    List<SNPost> getUserPostsDescending(String userName, PostVisibility postVisibility);

    /**
     *
     * @param snPost
     * @return
     */
    SNPost savePost(SNPost snPost);

    /**
     *
     * @param postId
     * @return the number of posts that will be deleted?
     */
    int removePost(ObjectId postId);

    /**
     * @param vote The vote that will be removed.
     * @return The post that has the vote, but after removing the vote from the votes list.
     */
    @Nullable
    SNPost removeVote(Vote vote);
}