package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userAccount.model.SNUserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @GetMapping("/signup")
    public String displaySignupForm(Model model) {
        model.addAttribute("user", new SNUserRegistrationDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView processSignupRequest(@ModelAttribute SNUserRegistrationDTO user) {
        ModelAndView modelAndView = new ModelAndView();
        HttpEntity<SNUserRegistrationDTO> httpEntity = new HttpEntity<>(user);
        try {
            ResponseEntity<SNUser> responseEntity =
                    restTemplate.exchange("http://user-service/register", HttpMethod.POST, httpEntity, SNUser.class);
            if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
                modelAndView.addObject("userCreated", true);
            }
        } catch (HttpClientErrorException e) {
            modelAndView.addObject("user", user);
            modelAndView.addObject("emailAlreadyExists", true);
        }
        modelAndView.setViewName("signup");
        return modelAndView;
    }
}