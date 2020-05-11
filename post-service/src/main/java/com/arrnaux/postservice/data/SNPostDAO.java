package com.arrnaux.postservice.data;

import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
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

    /**
     * @param nrOfPostToBeRetrieved
     * @param snPostId              is the last post displayed. It is a partial object. Only the id field will be populated.
     *                              If it's equal to -1, no posts has been displayed yet.
     * @param users                 represents a list of users ids.
     * @return
     */
    @Nullable
    List<SNPost> getMorePostsFromUsers(Integer nrOfPostToBeRetrieved, String snPostId, List<String> userIds,
                                       PostVisibility postVisibility);
}