package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
        // Retrieve only public post if the user is logged.
        String targetUrl = "http://post-service/timeline/showMorePosts/" + lastShowedId;
        return getTimelineModelAndView(loggedUser, targetUrl);
    }

    @RequestMapping(value = "showMoreFromSelf", method = RequestMethod.POST)
    public ModelAndView loadMorePostsFromSelf(HttpServletRequest request, @RequestBody String lastShowedId) {
        // The user is viewing his own profile, show private and public posts.
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");

        String targetUrl = "http://post-service/timeline/showMorePostsFromSelf/" + lastShowedId;
        return getTimelineModelAndView(loggedUser, targetUrl);
    }

    private ModelAndView getTimelineModelAndView(SNUser loggedUser, String targetUrl) {
        List<SNPost> posts = getPostsForUser(loggedUser, targetUrl);
        if (null != posts) {
            ModelAndView modelAndView = new ModelAndView()
                    .addObject("posts", posts)
                    .addObject("newComment", new Comment());
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