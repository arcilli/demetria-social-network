package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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

        // retrieve only public post if the logged user is the owner of the post
        // if the user is viewing his profile, find private and public posts
        // Make a request to post-service.
        String targetUrl = "http://post-service/timeline/showMorePosts/" + lastShowedId;
        List<SNPost> posts = restTemplate.exchange(targetUrl, HttpMethod.POST,
                new HttpEntity<>(loggedUser), new ParameterizedTypeReference<List<SNPost>>() {
                }).getBody();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.
                addObject("posts", posts)
                .addObject("newComment", new Comment())
                .setViewName("fragments/timeline :: timeline-part");
        return modelAndView;
    }
}