package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUserLoginDTO;
import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @GetMapping("/")
    public ModelAndView displayIndex(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView();
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            // the user is not logged
            modelAndView.addObject("userLoginDTO", new SNUserLoginDTO());
            modelAndView.addObject("newUser", new SNUserRegistrationDTO());
            modelAndView.setViewName("home/homeNotSignedIn");
        } else {
            // TODO: rename attribute to "newPost"
            modelAndView.addObject("post", new SNPost());
            modelAndView.setViewName("home/homeSignedIn");
        }
        return modelAndView;
    }

    @GetMapping("index")
    ModelAndView indexOnPath(HttpServletRequest httpServletRequest) {
        return displayIndex(httpServletRequest);
    }
}