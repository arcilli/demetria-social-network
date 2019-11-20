package com.arrnaux.frontend;

import com.arrnaux.userservice.userAccount.model.SNUserRegistrationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class SimpleController {

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

//        ResponseEntity<String> restExchange =
////                restTemplate.exchange(
////                        "http://user-service/register/", HttpMethod.POST, HttpEntity.EMPTY, String.class, user);
////        return "home";
        // TODO: decide on a type of response that need to be sent/received
        SNUserRegistrationDTO result = restTemplate.postForObject("http://user-service/register/", user, SNUserRegistrationDTO.class);

        return "home";
    }

//    @PostMapping("/greeting")
//    public String greetingSubmit(@ModelAttribute Greeting greeting) {
//        return "result";
//    }
}
