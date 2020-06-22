package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import com.arrnaux.frontend.util.friendship.FriendshipUtilsService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    final
    RestTemplate restTemplate;

    public RegisterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String displaySignUpForm(Model model) {
        // If the request comes as a redirect from a signupRequest processing, then the field is already filled with
        // the email. Otherwise, the current request will simply display the form, so a new object is needed.
        if (null == model.getAttribute("newUser")) {
            model.addAttribute("newUser", new SNUserRegistrationDTO());
        }
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView processSignUpRequest(@ModelAttribute SNUserRegistrationDTO user,
                                             RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            ResponseEntity<Boolean> responseEntity = UserUtilsService.executeRegisterRequest(user);
            if (responseEntity.getStatusCode() == HttpStatus.CREATED && null != responseEntity.getBody()) {
                SNUser persistedUser = UserUtilsService.getObfuscatedUserByUserName(user.getUserName());
                // Do not use a ribbon client here.
                if (FriendshipUtilsService.createPersonVertex(restTemplate, persistedUser)) {
                    redirectAttributes.addFlashAttribute("userCreated", true);
                }
            } else {
                if (responseEntity.getStatusCode() == HttpStatus.IM_USED && null == responseEntity.getBody()) {
                    redirectAttributes.addFlashAttribute("newUser", user);
                    redirectAttributes.addFlashAttribute("usernameAlreadyExists", true);
                }
            }
        } catch (HttpClientErrorException e) {
            System.out.println(e.toString());
            redirectAttributes.addFlashAttribute("newUser", user);
            redirectAttributes.addFlashAttribute("emailAlreadyExists", true);
        }
        modelAndView.setViewName("redirect:/signup");
        return modelAndView;
    }
    // TODO: add a checking for unique username.
}