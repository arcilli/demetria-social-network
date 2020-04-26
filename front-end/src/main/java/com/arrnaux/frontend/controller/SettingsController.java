package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("settings")
@Log
public class SettingsController {

    final
    RestTemplate restTemplate;

    public SettingsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ModelAndView displayUserInformation(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser) {
            // Fetch the user to ensure that it contains the newest profile picture.
            ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/users/info/id",
                    HttpMethod.POST, new HttpEntity<>(loggedUser.getId()), SNUser.class);
            if (null != responseEntity.getBody()) {
                loggedUser = responseEntity.getBody();
                // Overwrite the session attribute to contain newest picture.
                request.getSession().setAttribute("user", loggedUser);
            }

            // The modifiedUser is initialized with loggedUser.
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
        if (null != loggedUser) {
            log.info("User: " + loggedUser.toString() + "is changing his information.");
            loggedUser.updateObjectWithNotNullValues(modifiedUser);
            try {
                ResponseEntity<SNUser> responseEntity = restTemplate.exchange("http://user-service/settings/profile",
                        HttpMethod.POST, new HttpEntity<>(loggedUser), SNUser.class);
                // Invalidate current attribute from session since user settings was changed.
                request.getSession().removeAttribute("user");
                request.getSession().setAttribute("user", loggedUser);
                modelAndView.addObject("settingsUpdated", true);
                modelAndView.addObject("modifiedUser", loggedUser);
            } catch (Exception e) {
                e.printStackTrace();
                modelAndView.addObject("settingsUpdated", false);
            }
            modelAndView.setViewName("settings/profile");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    /**
     * Changes the profile picture for logged user. Returns the image encoded in Base64.
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "changeProfilePhoto", method = RequestMethod.POST)
    public String changeProfilePhoto(HttpServletRequest request, @RequestParam("profilePicture") MultipartFile profilePicture) {
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser) {
            try {
                // Send the image to user service.
                String targetUrl = "http://user-service/settings/changeProfileImage/" + loggedUser.getId();
                ResponseEntity<String> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST,
                        new HttpEntity<>(profilePicture.getBytes()), String.class);
                if (null != responseEntity.getBody()) {
                    return responseEntity.getBody();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // TODO: maybe receive only 2 objects (a SNUser - corresponding to oldPass) and a newUser (like the registration form
    // - with newPass and newPassMatch
    // TODO: To be implemented.
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public ModelAndView changePassword(HttpServletRequest request, @ModelAttribute String oldPassword,
                                       @ModelAttribute String newPassword, @ModelAttribute String newPasswordMatch) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
}