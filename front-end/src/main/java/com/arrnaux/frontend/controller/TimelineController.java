package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.frontend.util.posts.PostsUtilsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "timeline")
public class TimelineController {

    @RequestMapping(value = "showMore", method = RequestMethod.POST)
    public ModelAndView loadMorePosts(HttpServletRequest request, @RequestBody String lastShowedId) {
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        // Retrieve only public post if the user is not logged.
        return getTimelineModelAndView(loggedUser, lastShowedId);
    }

    @RequestMapping(value = "showMore/user/{userName}", method = RequestMethod.POST)
    public ModelAndView loadMoreFromUser(HttpServletRequest request, @PathVariable("userName") String userName,
                                         @RequestBody String lastShowedId) {
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        List<SNPost> posts;
        if (null != loggedUser && loggedUser.getUserName().equals(userName)) {
            // The user is viewing his own profile, so display private and public posts.
            posts = PostsUtilsService.getPostsFromUserFromUrl(lastShowedId, userName, PostVisibility.NONE);
        } else {
            // Somebody (user or outsider) is watching a profile. Only public posts will be displayed.
            posts = PostsUtilsService.getPostsFromUserFromUrl(lastShowedId, userName, PostVisibility.PUBLIC);
        }
        return createModelAndView(posts);
    }

    private ModelAndView getTimelineModelAndView(SNUser loggedUser, String lastShowedId) {
        List<SNPost> posts = PostsUtilsService.getPostsForUser(lastShowedId, loggedUser);
        return createModelAndView(posts);
    }

    private ModelAndView createModelAndView(List<SNPost> posts) {
        if (null != posts) {
            ModelAndView modelAndView = new ModelAndView();
            if (0 != posts.size()) {
                modelAndView
                        .addObject("posts", posts)
                        .addObject("newComment", new Comment());
            } else {
                modelAndView.addObject("noMorePosts", true);
            }
            modelAndView.setViewName("fragments/timeline :: timeline-part");
            return modelAndView;
        }
        return null;
    }
}