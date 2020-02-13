package com.arrnaux.user.service.services;

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

import java.util.ArrayList;
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

    // TODO: need to bring here a token for user/another method to authorize the actual request
    // returns the id of the post or null
    @Nullable
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String savePost(@RequestBody SNPost snPost) {
        try {
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

    // snPost arg will contain a single comment
    // returns the id of the last comment or null
    // TODO: this should be replaced with Mongo logic directly
    @RequestMapping(value = "createComment", method = RequestMethod.POST)
    public String appendCommentToExistingList(@RequestBody SNPost postWithReceivedComment) {
        try {
            if (postWithReceivedComment.getCommentList().size() == 1) {
                Comment receivedComment = postWithReceivedComment.getCommentList().get(0);

                // TODO: maybe retrieve only the list of comments
                SNPost persistedPost = snPostDAO.getPostById(postWithReceivedComment.getId());

                if (persistedPost == null) {
                    throw new Exception("The post does not exist.");
                }

                if (null == persistedPost.getCommentList()) {
                    persistedPost.setCommentList(new ArrayList<>());
                }

                receivedComment.setCreationDate(new Date());
                int commentNumber = persistedPost.getCommentList().size();
                receivedComment.setId(persistedPost.getId() + "-" + commentNumber);

                persistedPost.appendComment(postWithReceivedComment.getCommentList().get(0));
                persistedPost = snPostDAO.savePost(persistedPost);
                return persistedPost.getCommentList().get(persistedPost.getCommentList().size() - 1).getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // return a list of an users's post, sorted descending by date or null
    @Nullable
    @RequestMapping(value = "posts/user", method = RequestMethod.POST)
    public List getUserPostsDescending(@RequestBody String userId) {
        try {
            return snPostDAO.getUserPostsDateDesc(userId);
        } catch (Exception e) {
            log.severe(e.toString());
        }
        return null;
    }

    // return a post or null
    @Nullable
    @RequestMapping(value = "posts/{postId}", method = RequestMethod.GET)
    public SNPost servePostRequest(@PathVariable String postId) {
        try {
            SNPost snPost = snPostDAO.getPostById(postId);
            if (null != snPost) {
                return snPost;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @RequestMapping(value = "posts/user/{userName}", method = RequestMethod.POST)
    public List<SNPost> getUserPostsDescending(@PathVariable("userName") String userName, @RequestBody PostVisibility postVisibility) {
        try {
            return snPostDAO.getUserPostsDescending(userName, postVisibility);
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
            SNPost originalPost = snPostDAO.removeVote(currentVote);
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
