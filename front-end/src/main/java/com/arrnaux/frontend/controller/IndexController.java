package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUserLoginDTO;
import com.arrnaux.demetria.core.userAccount.model.SNUserRegistrationDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
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
    ModelAndView index(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView();
        if (httpServletRequest.getSession().getAttribute("user") == null) {
            // the user is not logged
            modelAndView.addObject("user", new SNUserRegistrationDTO());
            modelAndView.addObject("userLoginDTO", new SNUserLoginDTO());
            modelAndView.setViewName("home/homeNotSignedIn");
        } else {
            modelAndView.setViewName("home/homeSignedIn");
        }
        return modelAndView;
    }

    @GetMapping("index")
    ModelAndView indexOnPath(HttpServletRequest httpServletRequest) {
        return index(httpServletRequest);
    }
}