package com.arrnaux.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Logout {

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) {
        // if the user is logged-in
        ModelAndView modelAndView = new ModelAndView();
        if (request.getSession().getAttribute("email") != null) {
            request.getSession().invalidate();
        }
        modelAndView.setViewName("redirect:");
        return modelAndView;
    }
}
