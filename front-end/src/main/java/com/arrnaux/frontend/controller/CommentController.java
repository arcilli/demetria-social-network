package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CommentController {

    final
    RestTemplate restTemplate;

    public CommentController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param request
     * @param post       represents the post on which the user leaved a comment. Only the id of the content is used.
     * @param newComment is the comment that has to be stored. Only the content from this object is used. The owner is
     *                   set to the user that made the request.
     * @return the newly inserted comment with obfuscated owner or null if the operation ended with an error.
     */
    @Nullable
    @RequestMapping(value = "createComment", method = RequestMethod.POST)
    @ResponseBody
    public Comment createCommentForPost(HttpServletRequest request, SNPost post, Comment newComment) {
        // TODO: treat the case when the user is inserting an empty comment.
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            try {
                newComment.setOwnerId(currentUser.getId());
                post.appendComment(newComment);
                String targetUrl = "http://post-service/posts/createComment";
                ResponseEntity<Comment> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST,
                        new HttpEntity<>(post), Comment.class);
                if (null != responseEntity.getBody()) {
                    return responseEntity.getBody();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}