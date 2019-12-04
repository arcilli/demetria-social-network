package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PostController {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("createAPost")
    public ModelAndView showForm(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("post", new SNPost());
//        modelAndView.setViewName("#");
        return modelAndView;
    }

    @PostMapping("createAPost")
    public ModelAndView processPost(HttpServletRequest request, @ModelAttribute SNPost post) {
        // add a validation for a current user logged-in
        ModelAndView modelAndView = new ModelAndView();
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        post.setOwnerId(currentUser.getId());

        // make the request
        HttpEntity<SNPost> httpEntity = new HttpEntity<>(post);
        try {
            ResponseEntity<SNPost> responseEntity = restTemplate.exchange("http://user-service/postService",
                    HttpMethod.POST, httpEntity, SNPost.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // this return should not matter since it will be made with an ajax request
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}