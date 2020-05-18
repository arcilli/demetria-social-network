package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.frontend.util.friendship.FriendshipUtilsService;
import com.arrnaux.frontend.util.users.UserUtilsService;
import lombok.extern.java.Log;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

@Log
@RestController
@RequestMapping(value = "/profiles")
public class ProfileController {

    final
    RestTemplate restTemplate;

    public ProfileController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Everyone can see the public posts, no matter if it is logged or not.
    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public ModelAndView showUserProfile(HttpServletRequest request, @PathVariable("userName") String userName) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");

        SNUser profileOwner = UserUtilsService.getObfuscatedUserByUserName(userName);
        if (null != profileOwner) {
            // The userName is valid, so the owner of the profile exists.
            modelAndView
                    .addObject("profileOwner", profileOwner)
                    .addObject("userIsFollowed", profileIsFollowedByUser(profileOwner, loggedUser));

            if (-1 != FriendshipUtilsService.getNoFollowedUsers(profileOwner)) {
                modelAndView.addObject("noFollowedUsers", FriendshipUtilsService.getNoFollowedUsers(profileOwner));
            }
            if (-1 != FriendshipUtilsService.getNoFollowers(profileOwner)) {
                modelAndView.addObject("noFollowers", FriendshipUtilsService.getNoFollowers(profileOwner));
            }
            if (null != loggedUser && userName.equals(loggedUser.getUserName())) {
                modelAndView.addObject("post", new SNPost());
            }

            modelAndView.setViewName("profile");
        } else {
            // The user does not exist.
            // TODO: replace it with an explicit message (eg. no user found)
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "{userName}/followers")
    public ModelAndView showFollowers(HttpServletRequest request, @PathVariable("userName") String userName) {
        // Only logged persons will be able to see the followers.
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        if (null != loggedUser) {
            SNUser profileOwner = UserUtilsService.getObfuscatedUserByUserName(userName);
            if (null != profileOwner) {
                List<SNUser> followers = FriendshipUtilsService.getFollowers(profileOwner);
                HashMap<SNUser, Boolean> usersAndFollowedValue = null;
                if (null != followers) {
                    usersAndFollowedValue = getUsersAndFollowValue(loggedUser, followers);
                }
                modelAndView
                        .addObject("profileOwner", profileOwner)
                        .addObject("type", "Followers")
                        .addObject("users", usersAndFollowedValue)
                        .addObject("userIsFollowed", profileIsFollowedByUser(profileOwner, loggedUser))
                        .setViewName("profile/followers");
            }
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    @RequestMapping(value = "{userName}/following")
    public ModelAndView showFollowedPersons(HttpServletRequest request, @PathVariable("userName") String userName) {
        // Only logged persons will be able to see it.
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        if (null != loggedUser) {
            SNUser profileOwner = UserUtilsService.getObfuscatedUserByUserName(userName);
            if (null != profileOwner) {
                List<SNUser> users = FriendshipUtilsService.getFollowedUsers(
                        SNUser.builder()
                                .id(profileOwner.getId())
                                .build()
                );
                HashMap<SNUser, Boolean> usersAndFollowedValue = null;
                if (null != users) {
                    usersAndFollowedValue = getUsersAndFollowValue(loggedUser, users);
                }
                modelAndView
                        .addObject("profileOwner", profileOwner)
                        .addObject("type", "Followed persons")
                        .addObject("users", usersAndFollowedValue)
                        .addObject("userIsFollowed", profileIsFollowedByUser(profileOwner, loggedUser))
                        .setViewName("profile/followedPersons");
            }
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    @RequestMapping(value = "deleteAccount", method = RequestMethod.DELETE)
    public ModelAndView deleteAccount(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (loggedUser != null) {
            if (userWasDeletedFromGraphAndDocDB(loggedUser)) {
                // The account has been deleted.
                request.getSession().invalidate();
                modelAndView.setViewName("redirect:/");
            }
        } else {
            modelAndView.addObject("deleteAccountError", true);
            modelAndView.setViewName("deleteAccount");
        }
        return modelAndView;
    }

    /**
     * @param profileOwner NotNull, is the user that owns the profile.
     * @param user         the user for which is checked if is following the profileOwner. Null in the case when no user
     *                     is logged.
     * @return true if user is following profileOwner. Otherwise, false.
     */
    private boolean profileIsFollowedByUser(@NotNull SNUser profileOwner, @Nullable SNUser user) {
        if (null != user) {
            if (userIsTheOwnerOfTheProfile(profileOwner, user)) {
                // An user cannot follow himself.
                return false;
            }
            return FriendshipUtilsService.checkFollowRelation(user, profileOwner);
        }
        return false;
    }

    private boolean userIsTheOwnerOfTheProfile(@NotNull SNUser profileOwner, @NotNull SNUser user) {
        return profileOwner.getUserName().equals(user.getUserName());
    }

    private boolean userWasDeletedFromGraphAndDocDB(SNUser user) {
        // Delete the account from graph & after that delete it from document-oriented database.
        if (FriendshipUtilsService.deleteUserFromGraph(user)) {
            // Continue deleting the user from document-oriented DB.
            return UserUtilsService.deleteUser(user);
        }
        return false;
    }

    /**
     * @param currentOwner
     * @param users
     * @return a hashmap with the user as a key and a follow relation status (true if the user is followed, otherwise
     * false
     */
    private HashMap<SNUser, Boolean> getUsersAndFollowValue(SNUser currentOwner, List<SNUser> users) {
        HashMap<SNUser, Boolean> usersAndFollowValue = new HashMap<>();
        for (SNUser snUser : users) {
            if (null != snUser) {
                usersAndFollowValue.put(snUser, FriendshipUtilsService.checkFollowRelation(currentOwner, snUser));
            }
        }
        return usersAndFollowValue;
    }
}