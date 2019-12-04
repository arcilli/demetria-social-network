package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("settings")
public class SettingsController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ModelAndView dispalyUserInformation(HttpServletRequest request, ModelAndView modelAndView) {
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (loggedUser != null) {
            modelAndView.addObject("modifiedUser", loggedUser);
            modelAndView.setViewName("settings/profile");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    @RequestMapping(value = "profile", method = RequestMethod.POST)
    public ModelAndView processUserInformationUpdate(HttpServletRequest request, @ModelAttribute SNUser modifiedUser) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (loggedUser != null) {
            loggedUser.modifyPartialFieldsFromObject(modifiedUser);
            HttpEntity<SNUser> httpEntity = new HttpEntity<>(loggedUser);
            try {
                ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/settings/profile",
                        HttpMethod.POST, httpEntity, SNUser.class);
                // Invalidate current attribute from session since user settings was changed.
                request.getSession().removeAttribute("user");
                request.getSession().setAttribute("user", loggedUser);
                modelAndView.addObject("settingsUpdated", true);
                modelAndView.addObject("modifiedUser", loggedUser);
                modelAndView.setViewName("settings/profile");
            } catch (Exception e) {
                e.printStackTrace();
                modelAndView.addObject("settingsUpdated", false);
                modelAndView.setViewName("settings/profile");
            }
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    // TODO: to be implemented
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public ModelAndView changePassword(HttpServletRequest request, @ModelAttribute String oldPassword,
                                       @ModelAttribute String newPassword, @ModelAttribute String newPasswordMatch) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}
