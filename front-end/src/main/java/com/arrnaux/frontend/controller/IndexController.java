package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUserLoginDTO;
import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayIndex(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView();
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            // The user is not logged.
            modelAndView.addObject("userLoginDTO", new SNUserLoginDTO());
            modelAndView.addObject("newUser", new SNUserRegistrationDTO());
            modelAndView.setViewName("home/homeNotSignedIn");
        } else {
            // rename attribute to "newPost"?
            modelAndView.addObject("post", new SNPost());
            modelAndView.setViewName("home/homeSignedIn");
        }
        return modelAndView;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    ModelAndView indexOnPath(HttpServletRequest httpServletRequest) {
        return displayIndex(httpServletRequest);
    }
}