package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserLoginDTO;
import com.arrnaux.frontend.util.users.UserUtilsService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Log
public class LoginController {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLoginForm(HttpServletRequest httpServletRequest, Model model) {
        HttpSession session = httpServletRequest.getSession();
        if (null == session.getAttribute("user")) {
            // The user is not logged in, so the login form is displayed.
            if (null == model.getAttribute("userLoginDTO")) {
                model.addAttribute("userLoginDTO", new SNUserLoginDTO());
            }
            return "login";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView loginUser(HttpServletRequest request, @ModelAttribute SNUserLoginDTO userLoginDTO,
                                  RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        if (null == session.getAttribute("user")) {
            try {
                log.info("User " + userLoginDTO.getEmail() + " is trying to login.");
                SNUser loggedUser = UserUtilsService.executeLoginRequest(userLoginDTO);
                if (null != loggedUser) {
                    session.setAttribute("user", loggedUser.obfuscateUserInformation());
                }
                modelAndView.setViewName("redirect:/");
            } catch (HttpClientErrorException e) {
                redirectAttributes.addFlashAttribute("badCredentials", true);
                // Data in form should be persisted after an unsuccessful login, except the password.
                userLoginDTO.setPassword("");
                redirectAttributes.addFlashAttribute("userLoginDTO", userLoginDTO);
                modelAndView.setViewName("redirect:/login");
            }
        }
        return modelAndView;
    }
}
