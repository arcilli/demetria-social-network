package com.arrnaux.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyProfile {

    // see what information should an user
    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ModelAndView ceva(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}
