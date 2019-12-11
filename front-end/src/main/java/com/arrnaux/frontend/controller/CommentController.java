package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    // SNPost post will contain only the id
    @RequestMapping(value = "createComment", method = RequestMethod.POST)
    public ResponseEntity createCommentForPost(HttpServletRequest request, SNPost post, Comment comment) {
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            try {
                comment.setOwner(currentUser);
                // save only the newest comment
                post.appendComment(comment);
                HttpEntity<SNPost> httpEntity = new HttpEntity<>(post);

                // request to user-service
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}