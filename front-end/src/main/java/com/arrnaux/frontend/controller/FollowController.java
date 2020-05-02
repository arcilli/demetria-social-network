package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.frontend.util.friendship.FriendshipUtilsService;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("follow")
public class FollowController {

    final
    RestTemplate restTemplate;

    public FollowController(@LoadBalanced RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param httpServletRequest
     * @param userNameToBeFollowed
     * @return true if userName1 was successfully followed username2. Otherwise, return false.
     * Return a responseBody since it will be used with an AjaxRequest.
     */
    @RequestMapping(value = "user/{userNameToBeFollowed}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean followUser(HttpServletRequest httpServletRequest,
                              @PathVariable("userNameToBeFollowed") String userNameToBeFollowed) {
        SNUser loggedUser = (SNUser) httpServletRequest.getSession().getAttribute("user");
        if (null != loggedUser) {
            return FriendshipUtilsService.followUser(loggedUser, userNameToBeFollowed);
        }
        return false;
    }

    @RequestMapping(value = "cancel/{usernameToBeUnfollowed}", method = RequestMethod.POST)
    @ResponseBody
    public Boolean unfollowUser(HttpServletRequest httpServletRequest,
                                @PathVariable("usernameToBeUnfollowed") String usernameToBeUnfollowed) {
        SNUser loggedUser = (SNUser) httpServletRequest.getSession().getAttribute("user");
        if (null != loggedUser) {
            return FriendshipUtilsService.unfollowUser(loggedUser, usernameToBeUnfollowed);
        }
        return false;
    }
}
