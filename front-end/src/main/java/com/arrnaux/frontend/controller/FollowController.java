package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("follow")
public class FollowController {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    /**
     * @param httpServletRequest
     * @param userNameToBeFollowed
     * @return true if userName1 is following username2. Otherwise, return false.
     * Return a responseBody since it will be used with an AjaxRequest.
     */
    @RequestMapping(value = "user/{userNameToBeFollowed}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean followUser(HttpServletRequest httpServletRequest,
                              @PathVariable("userNameToBeFollowed") String userNameToBeFollowed) {
        SNUser loggedUser = (SNUser) httpServletRequest.getSession().getAttribute("user");
        if (null != loggedUser) {
            String targetUrl = "http://friendship-relation-service/follow/" + userNameToBeFollowed;
            ResponseEntity<Boolean> responseEntity =
                    restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(loggedUser.getUserName()),
                            Boolean.class);
            if (null != responseEntity.getBody()) {
                return responseEntity.getBody();
            }
        }
        return false;
    }
}
