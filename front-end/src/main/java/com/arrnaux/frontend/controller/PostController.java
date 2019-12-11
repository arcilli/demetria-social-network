package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PostController {

    @Autowired
    RestTemplate restTemplate;

    @PostMapping("createAPost")
    public ModelAndView processPost(HttpServletRequest request, @ModelAttribute SNPost post) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            post.setOwner(currentUser);
            // make the request
            HttpEntity<SNPost> httpEntity = new HttpEntity<>(post);
            try {
                ResponseEntity<SNPost> responseEntity = restTemplate.exchange("http://user-service/postService",
                        HttpMethod.POST, httpEntity, SNPost.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // this return should not matter since it will be made with an ajax request
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "/deletePost", consumes = "application/json", method = RequestMethod.DELETE)
    public ResponseEntity processPostDelete(HttpServletRequest request, @RequestBody SNPost post) {
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            try {
                HttpEntity<SNPost> httpEntity = new HttpEntity<>(post);
                String url = "http://user-service/postService/";
                return restTemplate.exchange(url,
                        HttpMethod.DELETE, httpEntity, Boolean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

}