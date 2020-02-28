package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userAccount.model.SNUserLoginDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final RestTemplate restTemplate;

    // This uses constructor injection to set the controller's final copy of restTemplate.
    public LoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLoginForm(Model model, HttpServletRequest httpServletRequest) {
        // Check the session
        // If the session has already an attribute on "user" redirect to home);
        HttpSession session = httpServletRequest.getSession();
        if (null == session.getAttribute("user")) {
            // the user not already logged in
            // display login form
            model.addAttribute("userLoginDTO", new SNUserLoginDTO());
            return "login";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ModelAndView loginUser(HttpServletRequest request, @ModelAttribute SNUserLoginDTO userLoginDTO) {
        ModelAndView modelAndView = new ModelAndView();
        HttpSession session = request.getSession();
        if (null == session.getAttribute("user")) {
            try {
                ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/login/form",
                        HttpMethod.POST, new HttpEntity<>(userLoginDTO), SNUser.class);
                if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
                    SNUser loggedUser = responseEntity.getBody();
                    // TODO: ensure that the loggedUser has no info about password
                    session.setAttribute("user", loggedUser);
                    modelAndView.setViewName("redirect:/");
                }
            } catch (HttpClientErrorException e) {
                modelAndView.addObject("badCredentials", true);
                // data in form should be persisted after an unsuccessful login, except the password
                userLoginDTO.setPassword("");
                modelAndView.addObject("userLoginDTO", userLoginDTO);
                modelAndView.setViewName("login");
            }
        }
        return modelAndView;
    }
}
