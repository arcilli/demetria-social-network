package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import com.arrnaux.frontend.util.users.UserUtilsService;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

// Use controller annotation since both models & view names will be returned.
@Controller
@RequestMapping("settings")
@Log
public class SettingsController {

    @RequestMapping(value = "profile", method = RequestMethod.GET)
    public String displayChangeSettingsForm(Model model, HttpServletRequest request) {
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
            model.addAttribute("modifiedUser", loggedUser);
            model.addAttribute("userRegistrationDTO", new SNUserRegistrationDTO());
            return "settings/profile";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "profile", method = RequestMethod.POST)
    public ModelAndView processUserInformationUpdate(HttpServletRequest request, @ModelAttribute SNUser modifiedUser,
                                                     RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser) {
            log.info("User: " + loggedUser.toString() + "is changing his information.");
            loggedUser.updateObjectWithNotNullValues(modifiedUser);
            try {
                UserUtilsService.executeUserSettingsChangeRequest(loggedUser);
                // Invalidate current attribute from session since user settings were changed.
                // The loggedUser object is now updated with desired changes.
                request.getSession().setAttribute("user", loggedUser);
                redirectAttributes.addFlashAttribute("settingsUpdated", true);
                redirectAttributes.addFlashAttribute("modifiedUser", loggedUser);
            } catch (Exception e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("settingsUpdated", false);
            }
            modelAndView.setViewName("redirect:/settings/profile");
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
    public String changeProfilePhoto(HttpServletRequest request, @RequestParam("profilePicture") String profilePicture) {
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

    /**
     * @param request
     * @param snUserRegistrationDTO is a partial populated object. It contains only password and passwordMatch fields.
     * @param oldPassword
     * @return
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public ModelAndView processPasswordChange(HttpServletRequest request,
                                              @ModelAttribute("snUserRegistrationDTO") SNUserRegistrationDTO snUserRegistrationDTO,
                                              @RequestParam("oldPassword") String oldPassword,
                                              RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser && null != snUserRegistrationDTO) {
            log.info("User: " + loggedUser.getUserName() + " is requested the change of its password.");
            if (snUserRegistrationDTO.getPassword().equals(snUserRegistrationDTO.getPasswordMatch())) {
                Map<String, Object> parameters = new HashMap<>();
                loggedUser.setPassword(oldPassword);
                parameters.put("oldUser", loggedUser);
                parameters.put("newPassword", snUserRegistrationDTO.getPassword());
                ResponseEntity<Boolean> passwordWasChangedResponse = UserUtilsService.changeUserPassword(parameters);
                if (null != passwordWasChangedResponse.getBody()) {
                    if (passwordWasChangedResponse.getBody()) {
                        redirectAttributes.addFlashAttribute("passWasChanged", true);
                        log.info("User: " + loggedUser.getUserName() + " has changed his password.");
                    } else if (HttpStatus.ACCEPTED == passwordWasChangedResponse.getStatusCode()) {
                        redirectAttributes.addFlashAttribute("wrongPassword", true);
                    } else if (HttpStatus.NO_CONTENT == passwordWasChangedResponse.getStatusCode()) {
                        redirectAttributes.addFlashAttribute("minimumOnePasswordIsMissing", true);
                    }
                }
            } else {
                // The new password and the confirmation new password does not match.
                redirectAttributes.addFlashAttribute("mismatchPassword", true);
            }
            modelAndView.setViewName("redirect:/settings/profile");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }
}