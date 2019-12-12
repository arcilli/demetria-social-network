package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {

    // Use the restTemplate declared in main Class
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    SNPostDAO snPostDAO;

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ModelAndView displayProfile(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (loggedUser != null) {
            // TODO: should load only a chunk from user posts
            String targetURL = "http://user-service/postService/posts/user";
            ResponseEntity<List<SNPost>> responseEntity = restTemplate.exchange(targetURL, HttpMethod.POST,
                    new HttpEntity<String>(loggedUser.getId()), new ParameterizedTypeReference<List<SNPost>>() {
                    });
            List<SNPost> userPosts = responseEntity.getBody();
            modelAndView.addObject("userPosts", userPosts);
            modelAndView.addObject("newComment", new Comment());
            modelAndView.setViewName("profile");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }
}
