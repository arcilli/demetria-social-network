package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
public class ProfileController {

    final
    RestTemplate restTemplate;

    public ProfileController(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "deleteAccount", method = RequestMethod.DELETE)
    public ModelAndView deleteAccount(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");
        if (loggedUser != null) {
            String targetURL = "http://user-service/settings/deleteAccount";
            ResponseEntity<Boolean> responseEntity = restTemplate.exchange(targetURL, HttpMethod.DELETE,
                    new HttpEntity<>(loggedUser), Boolean.class);
            if (null != responseEntity.getBody() && responseEntity.getBody()) {
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

    // TODO: issue #21: Secure endpoints, display register form when the user is not logged but is accessing a resource.
    // Everyone can see the public posts, no matter if it is logged or not.
    @RequestMapping(value = "/profiles/{userName}", method = RequestMethod.GET)
    public ModelAndView showUserProfile(HttpServletRequest request, @PathVariable("userName") String userName) {
        ModelAndView modelAndView = new ModelAndView();
        SNUser loggedUser = (SNUser) request.getSession().getAttribute("user");

        // Get information about the owner of the profile.
        String targetUrl = "http://user-service/users/info/" + userName;
        ResponseEntity<SNUser> snUserResponseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST,
                null, SNUser.class);
        SNUser profileOwner = snUserResponseEntity.getBody();

        if (null != profileOwner) {
            if (null != loggedUser && loggedUser.getUserName().equals(userName)) {
                posts = getPostsForUser(profileOwner.getUserName(), PostVisibility.NONE);
            } else {
                posts = getPostsForUser(profileOwner.getUserName(), PostVisibility.PUBLIC);
            }
            modelAndView.addObject("newComment", new Comment());
            modelAndView.addObject("profileOwner", profileOwner);
            assert null != posts;
            modelAndView.addObject("userPosts", posts);
        } else {
            // The user does not exists.
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    /**
     * @param profileOwner is NotNull, is the user that owns the profile.
     * @param user         is the user for which is checked if is following the profileOwner.
     * @return true if user is following profileOwner. Otherwise, false.
     */
    private boolean profileIsFollowedByUser(@NotNull SNUser profileOwner, SNUser user) {
        if (null != user) {
            if (userIsTheOwnerOfTheProfile(profileOwner, user)) {
                // An user cannot follow himself.
                return false;
            }

            String targetUrl = "http://friendship-relation-service/follow/check/" + user.getUserName() +
                    "/" + profileOwner.getUserName();
            ResponseEntity<Boolean> loggedUserFollowsProfileOwner = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, Boolean.class);
            if (null != loggedUserFollowsProfileOwner.getBody()) {
                modelAndView.addObject("userIsFollowed", loggedUserFollowsProfileOwner.getBody());
            }
        }
        return false;
    }

    private boolean userIsTheOwnerOfTheProfile(@NotNull SNUser profileOwner, @NotNull SNUser user) {
        return profileOwner.getUserName().equals(user.getUserName());
    }
}