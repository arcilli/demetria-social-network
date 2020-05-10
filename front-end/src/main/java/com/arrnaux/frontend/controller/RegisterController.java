package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import com.arrnaux.frontend.util.users.UserUtilsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegisterController {

    final
    RestTemplate restTemplate;

    public RegisterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String displaySignupForm(Model model) {
        model.addAttribute("newUser", new SNUserRegistrationDTO());
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView processSignupRequest(@ModelAttribute SNUserRegistrationDTO user) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            ResponseEntity<Boolean> responseEntity = UserUtilsService.executeRegisterRequest(user);
            if (responseEntity.getStatusCode() == HttpStatus.CREATED && null != responseEntity.getBody()) {
                modelAndView.addObject("userCreated", true);
            } else {
                if (responseEntity.getStatusCode() == HttpStatus.IM_USED && null == responseEntity.getBody()) {
                    modelAndView.addObject("newUser", user);
                    modelAndView.addObject("usernameAlreadyExists", true);
                }
            }
        } catch (HttpClientErrorException e) {
            System.out.println(e.toString());
            modelAndView.addObject("newUser", user);
            modelAndView.addObject("emailAlreadyExists", true);
        }
        modelAndView.setViewName("signup");
        return modelAndView;
    }
    // TODO: add a checking for unique username.
}