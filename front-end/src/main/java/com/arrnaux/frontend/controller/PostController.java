package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.frontend.util.friendship.FriendshipUtilsService;
import com.arrnaux.frontend.util.posts.PostsUtilsService;
import com.arrnaux.frontend.util.users.UserUtilsService;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@Log
public class PostController {

    @PostMapping("createAPost")
    public ModelAndView processPost(HttpServletRequest request, @ModelAttribute SNPost post) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            post.setOwnerId(currentUser.getId());
            try {
                log.info("User: " + currentUser.getId() + " is creating a post: " + post);
                PostsUtilsService.createPost(post);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // TODO: should return something to FE (display an error if the processing ended with an error).
        // This return should not matter since it will be made with an ajax request.
        try {
            String requestSource = new URI(request.getHeader(HttpHeaders.REFERER)).getPath();
            modelAndView.setViewName("redirect:" + requestSource);
        } catch (URISyntaxException e) {
            modelAndView.setViewName("redirect:/");
            log.severe(e.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/deletePost", consumes = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> processPostDelete(HttpServletRequest request, @RequestBody SNPost post) {
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (null != currentUser) {
            try {
                log.info("User " + currentUser.getId() + " is attempting to delete the post" + post.getId());
                SNPost currentPost = PostsUtilsService.getPost(post.getId());
                // The logged user should be the owner of the post.
                if (null != currentPost && currentUser.getId().equals(currentPost.getOwnerId()))
                    return PostsUtilsService.deletePost(post);
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
        SNPost snPost = PostsUtilsService.getPost(postId);
        if (null != snPost) {
            SNUser postOwner = null;
            if (null != snPost.getOwnerId()) {
                postOwner = UserUtilsService.getObfuscatedUserById(snPost.getOwnerId());
                // The post exists, so it will be displayed.
                modelAndView
                        .addObject("post", snPost)
                        .addObject("authorized", true);
            } else {
                // If the user is null, its account was deleted, so the post needs to be deleted as well.
                PostsUtilsService.deletePost(
                        SNPost.builder()
                                .id(postId)
                                .build());
            }
            switch (snPost.getVisibility()) {
                case PUBLIC:
                    if (null != postOwner) {
                        modelAndView.addObject("postOwner", postOwner);
                        if (null != loggedUser) {
                            // Only logged users can comment.
                            modelAndView.addObject("newComment", new Comment())
                                    .addObject("userIsFollowed",
                                            FriendshipUtilsService.checkFollowRelation(loggedUser, postOwner));
                        }
                    }
                    break;
                case PRIVATE:
                    // The logged user is viewing its own (private) post.
                    if (null != loggedUser && null != postOwner && loggedUser.getId().equals(postOwner.getId())) {
                        modelAndView
                                .addObject("newComment", new Comment())
                                // If the post is private and the user sees it, he's the owner of the post.
                                .addObject("postOwner", loggedUser);
                    } else {
                        modelAndView.addObject("authorized", false);
                    }
            }
            modelAndView.setViewName("posts/singlePost");
            return modelAndView;
        }
        modelAndView.addObject("authorized", false);
        modelAndView.setViewName("posts/singlePost");
        return modelAndView;
    }
}