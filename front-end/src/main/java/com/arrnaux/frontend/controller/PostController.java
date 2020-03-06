package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PostController {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @PostMapping("createAPost")
    public ModelAndView processPost(HttpServletRequest request, @ModelAttribute SNPost post) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            post.setOwnerId(currentUser.getId());
            try {
                restTemplate.exchange("http://post-service/posts/savePost", HttpMethod.POST,
                        new HttpEntity<>(post), String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // TODO: should return something to FE (display an error if the processing ended with an error).
        // This return should not matter since it will be made with an ajax request.
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    // TODO: add a validation (current user should be the owner of the post)
    @RequestMapping(value = "/deletePost", consumes = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> processPostDelete(HttpServletRequest request, @RequestBody SNPost post) {
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (null != currentUser) {
            try {
                // TODO: ensure that currentUser is the owner of the post
                String url = "http://post-service/posts/deletePost/";
                return restTemplate.exchange(url,
                        HttpMethod.DELETE, new HttpEntity<>(post), Boolean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "posts/{postId}", method = RequestMethod.GET)
    public ModelAndView displayPost(HttpServletRequest request, @PathVariable String postId) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        String requestURL = "http://post-service/posts/id/" + postId;
        ResponseEntity<SNPost> responseEntity = restTemplate.exchange(requestURL, HttpMethod.GET, HttpEntity.EMPTY,
                SNPost.class);
        SNPost snPost = responseEntity.getBody();
        if (null != snPost) {
            switch (snPost.getVisibility()) {
                case PUBLIC:
                    modelAndView
                            .addObject("authorized", true)
                            .addObject("post", snPost);
                    if (null != loggedUser) {
                        // Only logged users can comment.
                        modelAndView.addObject("newComment", new Comment());
                    } else {
                        // A guest can see the content, but can't comment.
                        modelAndView
                                .addObject("post", snPost)
                                .addObject("authorized", true);
                    }
                    modelAndView.setViewName("singlePost");
                    return modelAndView;
                case PRIVATE:
                    if (null != loggedUser) {
                        if (loggedUser.getId().equals(snPost.getOwner().getId())) {
                            modelAndView
                                    .addObject("post", snPost)
                                    .addObject("newComment", new Comment())
                                    .addObject("authorized", true)
                                    .setViewName("singlePost");
                            return modelAndView;
                        }
                    }
            }
        }
        modelAndView.addObject("authorized", false);
        modelAndView.setViewName("singlePost");
        return modelAndView;
    }
}