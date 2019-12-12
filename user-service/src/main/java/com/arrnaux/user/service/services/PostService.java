package com.arrnaux.user.service.services;

import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;

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
}
