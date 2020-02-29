package com.arrnaux.user.service.services;

import com.arrnaux.demetria.core.userAccount.data.SNUserDAO;
import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.PostVisibility;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import com.arrnaux.demetria.core.userPost.model.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Log

@RestController
@RequestMapping(value = "postService")
public class PostService {

    @Autowired
    private SNPostDAO snPostDAO;

    @Autowired
    private SNUserDAO snUserDAO;

    /**
     * @param snPost
     * @return the id of the saved post or null
     * TODO: need to bring here a token for user/another method to authorize the actual request.
     */
    @Nullable
    @RequestMapping(value = "", method = RequestMethod.POST)
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

    // delete a post by id
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Boolean deletePost(@RequestBody SNPost post) {
        try {
            int nrOfDeletedPosts = snPostDAO.removePost(post.getId());
            // delete operation ended with success
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
        SNUser snUser = snUserDAO.findById(receivedComment.getOwnerId());
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
    @RequestMapping(value = "posts/user", method = RequestMethod.POST)
    public List<SNPost> getUserPostsDescending(@RequestBody String userId) {
        try {
            // When the query will be executed, create a join on this.
            List<SNPost> posts = snPostDAO.getUserPostsDateDesc(userId);
            // Retrieve user information.
            SNUser snUser = snUserDAO.findById(userId);
            if (null != snUser) {
                // Add for every post the owner with obfuscated details.
                snUser.obfuscateUserInformation();
                // TODO: replace this with mongo joins
                for (SNPost snPost : posts) {
                    snPost.setOwner(snUser);
                    addOwnerToComment(snPost);
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
    @RequestMapping(value = "posts/{postId}", method = RequestMethod.GET)
    public SNPost getPostById(@PathVariable String postId) {
        try {
            SNPost snPost = snPostDAO.getPostById(postId);
            if (null != snPost) {
                SNUser snUser = snUserDAO.findById(snPost.getOwnerId());
                if (null != snUser) {
                    snUser.obfuscateUserInformation();
                    snPost.setOwner(snUser);
                    addOwnerToComment(snPost);
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
    @RequestMapping(value = "posts/user/{userName}", method = RequestMethod.POST)
    public List<SNPost> getUserPostsDescending(@PathVariable("userName") String userName, @RequestBody PostVisibility postVisibility) {
        try {
            SNUser snUser = snUserDAO.findUserByUsername(userName);
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
                        addOwnerToComment(post);
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
    @RequestMapping(value = "/posts/vote/", method = RequestMethod.POST)
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

    private void addOwnerToComment(@NotNull SNPost snPost) {
        List<Comment> comments = snPost.getCommentList();
        if (null != comments) {
            for (Comment comment : comments) {
                SNUser commentOwner = snUserDAO.findById(comment.getOwnerId());
                if (null != commentOwner) {
                    comment.setOwner(commentOwner);
                }
            }
        }
    }
}