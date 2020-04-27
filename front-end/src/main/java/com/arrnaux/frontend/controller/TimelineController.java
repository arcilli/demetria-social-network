package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "timeline")
public class TimelineController {

    final
    RestTemplate restTemplate;

    public TimelineController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "showMore", method = RequestMethod.POST)
    public ModelAndView loadMorePosts(HttpServletRequest request, @RequestBody String lastShowedId) {
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        // Retrieve only public post if the user is not logged.
        String targetUrl = "http://post-service/timeline/showMorePosts/" + lastShowedId;
        return getTimelineModelAndView(loggedUser, targetUrl);
    }

    @RequestMapping(value = "showMore/user/{userName}", method = RequestMethod.POST)
    public ModelAndView loadMoreFromUser(HttpServletRequest request, @PathVariable("userName") String userName,
                                         @RequestBody String lastShowedId) {
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");

        String targetUrl = "http://post-service/timeline/showMorePosts/" + lastShowedId + "/" + userName;
        List<SNPost> posts;
        if (null != loggedUser && loggedUser.getUserName().equals(userName)) {
            // The user is viewing his own profile, so display private and public posts.
            posts = restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(PostVisibility.NONE),
                    new ParameterizedTypeReference<List<SNPost>>() {
                    }).getBody();
        } else {
            // Somebody (user or outsider) is watching a profile. Only public posts will be displayed.
            posts = restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(PostVisibility.PUBLIC),
                    new ParameterizedTypeReference<List<SNPost>>() {
                    }).getBody();
        }
        return createModelAndView(posts);
    }

    private ModelAndView getTimelineModelAndView(SNUser loggedUser, String targetUrl) {
        List<SNPost> posts = getPostsForUser(loggedUser, targetUrl);
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

    @Nullable
    private List<SNPost> getPostsForUser(SNUser loggedUser, String targetUrl) {
        return restTemplate.exchange(targetUrl, HttpMethod.POST,
                new HttpEntity<>(loggedUser), new ParameterizedTypeReference<List<SNPost>>() {
                }).getBody();
    }
}