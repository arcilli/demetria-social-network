package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.frontend.util.users.UserUtilsService;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("settings")
@Log
public class SettingsController {

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public ModelAndView displayUserInformation(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser) {
            // Fetch the user to ensure that it contains the newest profile picture.
            SNUser userWithUpdatedPicture = UserUtilsService.getObfuscatedUserById(loggedUser.getId());
            if (null != userWithUpdatedPicture) {
                loggedUser = userWithUpdatedPicture;
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
                UserUtilsService.executeUserSettingsChangeRequest(loggedUser);
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
                ResponseEntity<String> responseEntity = UserUtilsService.executeChangePhotoRequest(profilePicture, loggedUser);
                if (null != responseEntity.getBody()) {
                    // Invalidate current session attribute.
                    loggedUser.setProfileImageBase64(responseEntity.getBody());
                    request.getSession().removeAttribute("user");
                    request.getSession().setAttribute("user", loggedUser);
                    return responseEntity.getBody();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // TODO: implement password change
}