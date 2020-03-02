package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("follow")
public class FollowController {

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @RequestMapping(value = "user/{userNameToBeFollowed}", method = RequestMethod.GET)
    public void followUser(HttpServletRequest httpServletRequest,
                           @PathVariable("userNameToBeFollowed") String userNameToBeFollowed) {
        SNUser loggedUser = (SNUser)httpServletRequest.getSession().getAttribute("user");
        if (null != loggedUser){
            String loggedUserName = loggedUser.getUserName();
            // make a request to followRelation
            String targetUrl = "http://friendship-relation-service/follow/"+userNameToBeFollowed;
            ResponseEntity<Boolean> responseEntity =
                    restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(loggedUserName), Boolean.class);
            System.out.print(responseEntity.getBody());
        }
    }
}
