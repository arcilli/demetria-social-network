package com.arrnaux.frontend.controller;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class VoteController {

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("votePost/{postId}")
    public String voteAPost(HttpServletRequest request, @PathVariable("postId") String postId, Vote newVote) {
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            try {
                newVote.setOwnerId(currentUser.getId());
                HttpEntity<Vote> httpEntity = new HttpEntity<>(newVote);
                ResponseEntity<Float> responseEntity = restTemplate.exchange("http://user-service//posts/vote/" + postId,
                        HttpMethod.POST, httpEntity, Float.class);
                if (responseEntity.getBody() != null) {
                    return responseEntity.getBody().toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Do something else
            // Return you are not allowed
            return null;
        }
        return null;
    }
}