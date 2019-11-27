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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Controller
//@Log4j
public class Login {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute SNUserLoginDTO userLoginDTO, Model model) {

        HttpEntity<SNUserLoginDTO> httpEntity = new HttpEntity<>(userLoginDTO);
        try {
            ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/login/form", HttpMethod.POST, httpEntity, SNUser.class);
            if (responseEntity.getStatusCode() == HttpStatus.ACCEPTED) {
                return "redirect:home";
                // send the session
            }
        } catch (HttpClientErrorException e) {
            model.addAttribute("badCredentials", true);
            // data should be persisted after a bad login
            userLoginDTO.setPassword("");
            model.addAttribute("userLoginDTO", userLoginDTO);
            return "login";
        }
        return "";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLoginForm(Model model) {
        // TODO: check the session, if user is already present, then redirect to home

        //else, displayLogin form
        model.addAttribute("userLoginDTO", new SNUserLoginDTO());
        return "login";
    }
}