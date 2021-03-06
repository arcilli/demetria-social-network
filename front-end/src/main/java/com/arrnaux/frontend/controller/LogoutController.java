package com.arrnaux.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LogoutController {

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        if (request.getSession().getAttribute("user") != null) {
            request.getSession().invalidate();
        }
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}
