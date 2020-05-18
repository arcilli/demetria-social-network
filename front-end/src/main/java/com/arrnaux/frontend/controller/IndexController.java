package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userAccount.SNUserLoginDTO;
import com.arrnaux.demetria.core.models.userAccount.SNUserRegistrationDTO;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.frontend.util.friendship.FriendshipUtilsService;
import com.arrnaux.frontend.util.users.UserUtilsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView displayIndex(HttpServletRequest httpServletRequest) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) httpServletRequest.getSession().getAttribute("user");
        if (null == loggedUser) {
            // The user is not logged.
            modelAndView.addObject("userLoginDTO", new SNUserLoginDTO());
            modelAndView.addObject("newUser", new SNUserRegistrationDTO());
            modelAndView.setViewName("home/homeNotSignedIn");
        } else {
            // rename attribute to "newPost"?
            List<SNUser> suggestedPersons = getSuggestedPersons(loggedUser);
            List<SNUser> popularPersons = getMostPopularPersons(loggedUser);
            Set<SNUser> usersToFollow = new HashSet<>();
            if (null != suggestedPersons) {
                usersToFollow.addAll(suggestedPersons);
            }
            if (null != popularPersons) {
                usersToFollow.addAll(popularPersons);
            }
            usersToFollow.remove(loggedUser);
            modelAndView.addObject("suggestedPersons", usersToFollow);
            modelAndView.addObject("post", new SNPost());
            modelAndView.setViewName("home/homeSignedIn");
        }
        return modelAndView;
    }

    @RequestMapping(value = "index", method = RequestMethod.GET)
    ModelAndView indexOnPath(HttpServletRequest httpServletRequest) {
        return displayIndex(httpServletRequest);
    }

    private List<SNUser> getSuggestedPersons(SNUser loggedUser) {
        List<String> ids = FriendshipUtilsService.getSuggestedIds(loggedUser);
        if (null != ids) {
            return ids.stream()
                    .map(UserUtilsService::getObfuscatedUserById)
                    .collect(Collectors.toList());
        }
        return null;
    }

    private List<SNUser> getMostPopularPersons(SNUser loggedUser) {
        List<String> ids = FriendshipUtilsService.getMostPopularWhoAreNotAlreadyFollowed(loggedUser);
        if (null != ids) {
            return ids.stream()
                    .map(UserUtilsService::getObfuscatedUserById)
                    .collect(Collectors.toList());
        }
        return null;
    }
}