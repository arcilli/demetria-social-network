package com.arrnaux.frontend.controller;

import com.arrnaux.userservice.userAccount.model.SNUser;
import com.arrnaux.userservice.userAccount.model.SNUserLoginDTO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

@Controller
@Log4j
public class Login {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginUser(@ModelAttribute SNUserLoginDTO userLoginDTO) {

        System.out.println(userLoginDTO);
        HttpEntity<SNUserLoginDTO> httpEntity = new HttpEntity<>(userLoginDTO);
        ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/login/form", HttpMethod.POST, httpEntity, SNUser.class);

        System.out.println(responseEntity.toString());
        return "home";
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String displayLoginForm(Model model) {
        model.addAttribute("userLoginDTO", new SNUserLoginDTO());

        return "login";
    }
}
