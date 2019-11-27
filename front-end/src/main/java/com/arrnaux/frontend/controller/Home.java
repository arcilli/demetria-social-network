package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Home {

    //TODO: decide on some arguments that the functions needs to receive
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String showHome(Model model) {

        // TODO: this should be populated after login with a SNUserDTO & a sessionid
        SNUserDTO snUserDTO = null;
        model.addAttribute("numeUser", "Gicu de la spalatorie");
        return "home";
    }
}