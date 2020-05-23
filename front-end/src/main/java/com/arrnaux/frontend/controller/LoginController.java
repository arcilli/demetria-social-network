package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserLoginDTO;
import com.arrnaux.frontend.util.users.UserUtilsService;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@Log
public class LoginController {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public ModelAndView displayLoginForm(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession();
        ModelAndView modelAndView = new ModelAndView();
        if (null == session.getAttribute("user")) {
            // The user not already logged in.
            // Display login form.
            modelAndView.addObject("userLoginDTO", new SNUserLoginDTO());
            modelAndView.setViewName("login");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView loginUser(HttpServletRequest request, @ModelAttribute SNUserLoginDTO userLoginDTO) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        if (null == session.getAttribute("user")) {
            try {
                log.info("User " + userLoginDTO + " is trying to login.");
                SNUser loggedUser = UserUtilsService.executeLoginRequest(userLoginDTO);
                if (null != loggedUser) {
                    session.setAttribute("user", loggedUser.obfuscateUserInformation());
                }
                modelAndView.setViewName("redirect:/");
            } catch (HttpClientErrorException e) {
                modelAndView.addObject("badCredentials", true);
                // Data in form should be persisted after an unsuccessful login, except the password.
                userLoginDTO.setPassword("");
                modelAndView.addObject("userLoginDTO", userLoginDTO);
                modelAndView.setViewName("login");
            }
        }
        return modelAndView;
    }
}
