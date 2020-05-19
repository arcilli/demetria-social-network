package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.frontend.util.posts.PostsUtilsService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
        modelAndView.setViewName("redirect:/");
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
        SNPost snPost = PostsUtilsService.displayPost(postId).getBody();
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
                    modelAndView.setViewName("posts/singlePost");
                    return modelAndView;
                case PRIVATE:
                    if (null != loggedUser) {
                        if (loggedUser.getId().equals(snPost.getOwner().getId())) {
                            modelAndView
                                    .addObject("post", snPost)
                                    .addObject("newComment", new Comment())
                                    .addObject("authorized", true)
                                    .setViewName("posts/singlePost");
                            return modelAndView;
                        }
                    }
            }
        }
        modelAndView.addObject("authorized", false);
        modelAndView.setViewName("posts/singlePost");
        return modelAndView;
    }
}