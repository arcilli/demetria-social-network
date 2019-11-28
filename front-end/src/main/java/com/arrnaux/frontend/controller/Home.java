package com.arrnaux.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class Home {

    //TODO: decide on some arguments that the functions needs to receive
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public ModelAndView showHome(HttpServletRequest httpServletRequest) {
        // TODO: this should be populated after login with a SNUserDTO & a sessionid
        ModelAndView modelAndView = new ModelAndView();
        if (httpServletRequest.getSession().getAttribute("user") != null) {
            // user is logged in
//            modelAndView.addAttribute("numeUser", "Gicu de la spalatorie");
            modelAndView.setViewName("home");
            modelAndView.addObject("numeUser", "Gicu de la spalatorie");
        } else {
            modelAndView.setViewName("redirect:");
        }
        return modelAndView;
    }
}