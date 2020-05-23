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
import org.springframework.web.multipart.MultipartFile;
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

    @RequestMapping(value = "changePassword", method = RequestMethod.GET)
    public String displayChangePropertyForm(Model model, HttpServletRequest request) {
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser) {
            model.addAttribute("userRegistrationDTO", new SNUserRegistrationDTO());
            return "settings/changePass";
        }
        return ("redirect:/");
    }

    /**
     * @param request
     * @param snUserRegistrationDTO is a partial populated object. It contains only password and passwordMatch fields.
     * @param oldPassword
     * @return
     */
    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public ModelAndView changePassword(HttpServletRequest request,
                                       @ModelAttribute("snUserRegistrationDTO") SNUserRegistrationDTO snUserRegistrationDTO,
                                       @RequestParam("oldPassword") String oldPassword,
                                       RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (null != loggedUser && null != snUserRegistrationDTO) {
            log.info("User " + loggedUser + "is changing the password");
            if (snUserRegistrationDTO.getPassword().equals(snUserRegistrationDTO.getPasswordMatch())) {
                Map<String, Object> parameters = new HashMap<>();
                loggedUser.setPassword(oldPassword);
                parameters.put("oldUser", loggedUser);
                parameters.put("newPassword", snUserRegistrationDTO.getPassword());
                ResponseEntity<Boolean> result = UserUtilsService.changeUserPassword(parameters);
                if (null != result.getBody()) {
                    if (result.getBody()) {
                        redirectAttributes.addFlashAttribute("passWasChanged", true);
                    } else if (HttpStatus.ACCEPTED == result.getStatusCode()) {
                        redirectAttributes.addFlashAttribute("wrongPassword", true);
                    } else if (HttpStatus.NO_CONTENT == result.getStatusCode()) {
                        redirectAttributes.addFlashAttribute("minimumOnePasswordIsMissing", true);
                    }
                    modelAndView.setViewName("redirect:/settings/changePassword");
                    return modelAndView;
                }
            }
        }
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }
}