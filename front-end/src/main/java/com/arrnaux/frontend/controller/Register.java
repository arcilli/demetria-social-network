package com.arrnaux.frontend.controller;

import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userAccount.model.SNUserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class Register {

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/signup")
    public String displaySignupForm(Model model) {
        model.addAttribute("user", new SNUserRegistrationDTO());
        return "signup";
    }

    // this should make a call for user-register service
    @PostMapping("/signup")
    public String processSignupRequest(@ModelAttribute SNUserRegistrationDTO user) {
        // do something here
        // TODO: decide on a type of response that need to be sent/receive


        //this below works & returns a SNUSer object
        //SNUser snUser = restTemplate.postForObject("http://user-service/register", user, SNUserRegistrationDTO.class);
//        ResponseEntity<String> entity = restTemplate.getForEntity("http://user-service/register", String.class);

        // This seems also to work
        HttpEntity<SNUserRegistrationDTO> httpEntity = new HttpEntity<>(user);
        ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/register", HttpMethod.POST, httpEntity, SNUser.class);


        // should populate the model with the user that is registered / logged in?!?
        return "home";
    }
}