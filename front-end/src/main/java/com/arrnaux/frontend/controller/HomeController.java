package com.arrnaux.frontend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
//
//        {
//        modelAndView.setViewName("redirect:");
//    } else
//
////TODO: decide on some arguments that the functions needs to receive
//    @RequestMapping(value = "/home", method = RequestMethod.GET)
//    public ModelAndView showHome(HttpServletRequest httpServletRequest) {
//        ModelAndView modelAndView = new ModelAndView();
//
//        // user is logged in
////            modelAndView.addAttribute("numeUser", "Gicu de la spalatorie");
//        modelAndView.setViewName("home");
//        modelAndView.addObject("numeUser", "Gicu de la spalatorie");
//    }
//        return modelAndView;
//}

    @GetMapping("/")
    String index(HttpServletRequest httpServletRequest) {
        return ((httpServletRequest.getSession().getAttribute("user") != null) ?
                "home/homeSignedIn" : "home/homeNotSignedIn");
    }
}