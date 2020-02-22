package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.PostVisibility;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @Autowired
    SNPostDAO snPostDAO;

    // TODO: decide if this is needed or not
    // Consider that an user should see all his posts
    /*
    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ModelAndView displayProfile(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (loggedUser != null) {
            // TODO: should load only a chunk from user posts
            populateWithUserPosts(modelAndView, loggedUser);
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }
     */

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

    @RequestMapping(value = "/profiles/{userName}", method = RequestMethod.GET)
    public ModelAndView showUserProfile(HttpServletRequest request, @PathVariable("userName") String userName) {
        ModelAndView modelAndView = new ModelAndView();

        // depending of the status of an user, this should display:
        /*
            - public posts & info (no matter the user is logged or not)
            - ONLY_FRIENDS posts if user is a fried of the owner
            - ONLY_ME if current logged user is the owner
         */
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser) {
            if (loggedUser.getUserName().equals(userName)) {
                populateWithUserPosts(modelAndView, loggedUser);
            } else {
                // add a checking if the users are friends
                // if yes, display public+only friends posts
                // else, display public posts with comment option
            }
        } else {
            // user is no logged
            // display only public posts without comment option
            // make a request for public posts
            // display it
            String targetUrl = "http://user-service/postService/posts/user/" + userName;
            ResponseEntity<List<SNPost>> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST,
                    new HttpEntity<PostVisibility>(PostVisibility.PUBLIC), new ParameterizedTypeReference<List<SNPost>>() {
                    });
            List<SNPost> userPosts = responseEntity.getBody();
            modelAndView.addObject("userPosts", userPosts);
            modelAndView.setViewName("profile");
        }
        return modelAndView;
    }

    private void populateWithUserPosts(ModelAndView modelAndView, SNUser loggedUser) {
        String targetURL = "http://user-service/postService/posts/user";
        ResponseEntity<List<SNPost>> responseEntity = restTemplate.exchange(targetURL, HttpMethod.POST,
                new HttpEntity<>(loggedUser.getId()), new ParameterizedTypeReference<List<SNPost>>() {
                });
        List<SNPost> userPosts = responseEntity.getBody();
        if (null != userPosts) {
            modelAndView.addObject("userPosts", userPosts);
        }
        modelAndView.addObject("newComment", new Comment());
        modelAndView.setViewName("profile");
    }

}
