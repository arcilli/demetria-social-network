package com.arrnaux.demetria.core.userPost.data;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.PostVisibility;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import com.arrnaux.demetria.core.userPost.model.Vote;
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
     * @return a list of posts owned by user
     */
    List<SNPost> getUserPosts(SNUser snUser);

    /**
     * @param ownerId
     * @return
     */
    List<SNPost> getUserPostsDateDesc(String ownerId);

    List<SNPost> getUserPostsDateDesc(String ownerId, PostVisibility postVisibility);

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
    SNPost removeVoteGivenByUser(String postId, String userId);

    /**
     * @param snPost
     * @param comment
     * @return
     */
    Comment addCommentToPost(Comment comment, SNPost snPost);

    /**
     * @param snPost
     * @return
     */
    Integer getLastCommentIndexForPost(SNPost snPost);
}