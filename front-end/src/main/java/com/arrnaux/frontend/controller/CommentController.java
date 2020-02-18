package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CommentController {

    RestTemplate restTemplate = new RestTemplate();

    // TODO: this is a work-around
    // SNPost post will contain only the id, with a garbage value of content
    // Comment content from newComment is the value that has to be stored.
    @RequestMapping(value = "createComment", method = RequestMethod.POST)
    public ResponseEntity createCommentForPost(HttpServletRequest request, SNPost post, Comment newComment) {
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            try {
                newComment.setOwner(currentUser.getId());
                post.appendComment(newComment);
                HttpEntity<SNPost> httpEntity = new HttpEntity<>(post);
                ResponseEntity<String> responseEntity = restTemplate.exchange("http://user-service/postService/createComment",
                        HttpMethod.POST, httpEntity, String.class);
                return new ResponseEntity<>(true, HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}