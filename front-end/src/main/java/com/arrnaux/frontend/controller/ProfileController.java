package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @RequestMapping(value = "deleteAccount", method = RequestMethod.DELETE)
    public ModelAndView deleteAccount(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (loggedUser != null) {
            String targetURL = "http://user-service/settings/deleteAccount";
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(targetURL, HttpMethod.DELETE,
                    new HttpEntity<>(loggedUser), Boolean.class);
            if (null != responseEntity.getBody() && responseEntity.getBody()) {
                // account has been deleted
                request.getSession().invalidate();
                modelAndView.setViewName("redirect:/");
            }
        } else {
            modelAndView.addObject("deleteAccountError", true);
            modelAndView.setViewName("deleteAccount");
        }
        return modelAndView;
    }

    //    TODO: now the entire collection is displayed. Get only a chunk and dispaly a button for mode
    @RequestMapping(value = "/profiles/{userName}", method = RequestMethod.GET)
    public ModelAndView showUserProfile(HttpServletRequest request, @PathVariable("userName") String userName) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        SNUser profileOwner = null;
        List<SNPost> posts = null;
        if (null != loggedUser) {
            if (loggedUser.getUserName().equals(userName)) {
                profileOwner = loggedUser.obfuscateUserInformation();
                posts = getPostsForUser(profileOwner.getUserName(), PostVisibility.NONE);
            }
        }
        // A logged/non-logged user, can see the public posts.
        // Retrieve owner user information.
        String targetUrl = "http://user-service/users/info/" + userName;
        ResponseEntity<SNUser> snUserResponseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST,
                null, SNUser.class);
        profileOwner = snUserResponseEntity.getBody();
        if (null != profileOwner) {
            posts = getPostsForUser(profileOwner.getUserName(), PostVisibility.PUBLIC);
            modelAndView.addObject("profileOwner", profileOwner);
            modelAndView.addObject("userPosts", posts);
            modelAndView.addObject("newComment", new Comment());

            // Check if the profileOwner is followed by the logged user.
            if (null != loggedUser && !loggedUser.getUserName().equals(profileOwner.getUserName())) {
                targetUrl = "http://friendship-relation-service/follow/check/" + loggedUser.getUserName() +
                        "/" + profileOwner.getUserName();
                ResponseEntity<Boolean> loggedUserFollowsProfileOwner = restTemplate.exchange(targetUrl, HttpMethod.GET,
                        HttpEntity.EMPTY, Boolean.class);
                if (null != loggedUserFollowsProfileOwner.getBody()) {
                    modelAndView.addObject("userIsFollowed", loggedUserFollowsProfileOwner.getBody());
                }
            }
        }
        modelAndView.setViewName("profile");
        return modelAndView;
    }

    @Nullable
    private List<SNPost> getPostsForUser(String username, @NotNull PostVisibility postVisibility) {
        String targetURL = "http://user-service/postService/posts/user/" + username;
        if (null != postVisibility) {
            return (restTemplate.exchange(targetURL, HttpMethod.POST,
                    new HttpEntity<>(postVisibility), new ParameterizedTypeReference<List<SNPost>>() {
                    })).getBody();
        }
        return null;
    }
}
