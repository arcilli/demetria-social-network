package com.arrnaux.postservice.services;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.demetria.core.models.userPost.Vote;
import com.arrnaux.postservice.Helper.UserAsOwnerOperations;
import com.arrnaux.postservice.data.SNPostDAO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Log

@RestController
@RequestMapping("posts")
public class PostService {

    private SNPostDAO snPostDAO;

    private RestTemplate restTemplate;

    private UserAsOwnerOperations userAsOwnerOperations;

    @Autowired
    public PostService(SNPostDAO snPostDAO, RestTemplate restTemplate, UserAsOwnerOperations userAsOwnerOperations) {
        this.snPostDAO = snPostDAO;
        this.restTemplate = restTemplate;
        this.userAsOwnerOperations = userAsOwnerOperations;
    }


    /**
     * @param snPost
     * @return the id of the saved post or null
     */
    @Nullable
    @RequestMapping(value = "savePost", method = RequestMethod.POST)
    public String savePost(@RequestBody SNPost snPost) {
        try {
            // TODO: replace now() with DB operation
            snPost.setCreationDate(new Date());
            SNPost savedPost = snPostDAO.savePost(snPost);
            return savedPost.getId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param post represents an object in which only id field is not null.
     * @return deletes a post by id
     */
    @RequestMapping(value = "deletePost", method = RequestMethod.DELETE)
    public Boolean deletePost(@RequestBody SNPost post) {
        try {
            int nrOfDeletedPosts = snPostDAO.removePost(post.getId());
            // Delete operation ended with success.
            if (nrOfDeletedPosts == 1) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param postWithReceivedComment is a post that contains a single comment (the one submitted by user).
     * @return a Comment object with obfuscated owner field set.
     */
    @Nullable
    @RequestMapping(value = "createComment", method = RequestMethod.POST)
    public Comment appendCommentToExistingList(@RequestBody SNPost postWithReceivedComment) {
        Comment receivedComment = postWithReceivedComment.getCommentList().get(0);
        SNUser snUser = userAsOwnerOperations.requestForSNUser(
                SNUser.builder()
                        .id(receivedComment.getOwnerId())
                        .build()
        );
        try {
            if (null != snUser) {
                // Get the last id & increment it.
                int noComments = snPostDAO.getLastCommentIndexForPost(postWithReceivedComment);
                receivedComment.setId(postWithReceivedComment.getId() + "-" + (++noComments));
                Comment storedComment = snPostDAO.addCommentToPost(receivedComment, postWithReceivedComment);
                if (null != storedComment) {
                    storedComment.setOwner(snUser.obfuscateUserInformation());
                    return storedComment;
                }
            }
        } catch (Exception e) {
            log.severe(e.toString());
        }
        return null;
    }

    /**
     * @param userId
     * @return a list of an users's post, sorted descending by date or null
     */
    @Nullable
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public List<SNPost> getUserPostsDescending(@RequestBody String userId) {
        try {
            List<SNPost> posts = snPostDAO.getUserPostsDateDesc(userId);
            // Retrieve user information.
            SNUser snUser = userAsOwnerOperations.requestForSNUser
                    (SNUser.builder()
                            .id(userId)
                            .build()
                    );
            if (null != snUser) {
                // Add for every post the owner with obfuscated details.
                for (SNPost snPost : posts) {
                    snPost.setOwner(snUser);
                    userAsOwnerOperations.addOwnerToComment(snPost);
                }
                return posts;
            }
        } catch (Exception e) {
            log.severe(e.toString());
        }
        return null;
    }

    /**
     * @param postId
     * @return a post or null
     * Display a post (accessed by permalink) with associated comments & votes.
     */
    @Nullable
    @RequestMapping(value = "id/{postId}", method = RequestMethod.GET)
    public SNPost getPostById(@PathVariable String postId) {
        try {
            SNPost snPost = snPostDAO.getPostById(postId);
            if (null != snPost) {
                SNUser snUser = userAsOwnerOperations.requestForSNUser
                        (SNUser.builder()
                                .id(snPost.getOwnerId())
                                .build()
                        );
                if (null != snUser) {
                    snUser.obfuscateUserInformation();
                    snPost.setOwner(snUser);
                    userAsOwnerOperations.addOwnerToComment(snPost);
                    return snPost;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param userName
     * @param postVisibility
     * @return the posts owned by the user, in descending order.
     * If postVisibility is PostVisibiliy.None, return both private & public posts.
     */
    @Nullable
    @RequestMapping(value = "/user/{userName}", method = RequestMethod.POST)
    public List<SNPost> getUserPostsDescending(@PathVariable("userName") String userName,
                                               @RequestBody PostVisibility postVisibility) {
        try {
            SNUser snUser = userAsOwnerOperations.requestForSNUser
                    (SNUser.builder()
                            .userName(userName)
                            .build());
            List<SNPost> posts;
            if (null != snUser) {
                snUser.obfuscateUserInformation();
                if (PostVisibility.NONE == postVisibility) {
                    // Retrieve all the posts.
                    posts = getUserPostsDescending(snUser.getId());
                } else {
                    posts = snPostDAO.getUserPostsDateDesc(snUser.getId(), postVisibility);
                }
                if (null != posts) {
                    for (SNPost post : posts) {
                        post.setOwner(snUser);
                        userAsOwnerOperations.addOwnerToComment(post);
                    }
                }
                return posts;
            }
        } catch (Exception e) {
            log.severe(e.toString());
        }
        return null;
    }

    /**
     * @param currentVote is the vote given by the user
     * @return the average vote score after considering the current vote
     * Takes the current vote and replace it in votes list corresponding to the post if the user has already voted for
     * the post. Otherwise, append the vote to the vote list.
     */
    @RequestMapping(value = "/vote/", method = RequestMethod.POST)
    public Double voteAPost(@RequestBody Vote currentVote) {
        try {
            SNPost originalPost = snPostDAO.removeVoteGivenByUser(currentVote.getPostId(), currentVote.getOwnerId());
            if (null != originalPost) {
                originalPost.appendVote(currentVote);
                // This is actually an update.
                originalPost.computeAverageRank();
                // Return an updated value for post rank.
                snPostDAO.savePost(originalPost);
                return originalPost.getAverageRank();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (double) -1;
    }
}