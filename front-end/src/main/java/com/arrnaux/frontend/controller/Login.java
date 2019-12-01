package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userAccount.model.SNUserLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
//@Log4j
public class Login {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute SNUserLoginDTO userLoginDTO, Model model, HttpServletRequest request) {
        HttpEntity<SNUserLoginDTO> httpEntity = new HttpEntity<>(userLoginDTO);
        HttpSession session = request.getSession();

        // the user not already logged in
        if (null == session.getAttribute("user")) {
            try {
                ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/login/form",
                        HttpMethod.POST, httpEntity, SNUser.class);
                if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
                    SNUser loggedUser = responseEntity.getBody();

                    // TODO: assure that the loggedUser has no info about password
                    session.setAttribute("user", loggedUser);
                    return "redirect:/";
                    // send the session
                }
            } catch (HttpClientErrorException e) {
                model.addAttribute("badCredentials", true);
                // data in form should be persisted after an unsuccessful login
                userLoginDTO.setPassword("");
                model.addAttribute("userLoginDTO", userLoginDTO);
                return "login";
            }
        } else {
            // should never getting here
            // a user that is already logged-in should never make a post to this
            return "redirect:/";
        }
        return "";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLoginForm(Model model, HttpServletRequest httpServletRequest) {
        // TODO: check the session, if user is already present, then redirect to homeO);
        HttpSession session = httpServletRequest.getSession();

        // the user not already logged in
        if (null == session.getAttribute("user")) {
            //else, displayLogin form
            model.addAttribute("userLoginDTO", new SNUserLoginDTO());
            return "login";
        } else {
            // the user is already logged-in, redirect to home
            return "redirect:home";
        }
    }
}
