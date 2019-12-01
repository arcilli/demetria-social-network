//package com.arrnaux.frontend.controller;
//
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.http.HttpServletRequest;
//
//public class Index {
//    // if the user is logged-in, then it should redirect to home
//    // else, display a page with a login form & a register one -FB style
//
//    @RequestMapping(value = "", method = RequestMethod.GET)
//    public ModelAndView showDefaultIndex(HttpServletRequest httpServletRequest) {
//        // if the user is already logged in
//        ModelAndView modelAndView = new ModelAndView();
//        if (httpServletRequest.getSession().getAttribute("email") != null) {
//            modelAndView.setViewName("redirect:home");
//        } else {
//            modelAndView.setViewName("index");
//        }
//        return modelAndView;
//    }
//
//    // TODO: this is not working in this form
//    @RequestMapping(value = "index", method = RequestMethod.GET)
//    public ModelAndView showIndexPage(HttpServletRequest httpServletRequest) {
//        return showDefaultIndex(httpServletRequest);
//    }
//}
