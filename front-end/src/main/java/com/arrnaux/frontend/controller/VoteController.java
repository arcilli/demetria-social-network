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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Controller
public class VoteController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * @param request
     * @param postId    represents the post which was voted.
     * @param voteValue raw value of the vote
     * @return the average value of the post, considering the actual vote
     */
    //    TODO: if the user has already voted this post, replace his vote and re-compute the average
    @RequestMapping(value = "votePost/{postId}/{voteValue}", method = RequestMethod.POST)
    public @ResponseBody
    Double voteAPost(HttpServletRequest request, @PathVariable("postId") String postId, @PathVariable("voteValue") int voteValue) {
        SNUser currentUser = (SNUser) request.getSession().getAttribute("user");
        if (currentUser != null) {
            try {
                Vote newVote = new Vote(postId, currentUser.getId(), voteValue);
                ResponseEntity<Object> responseEntity = restTemplate.exchange("http://user-service/postService/posts/vote/",
                        HttpMethod.POST, new HttpEntity<>(newVote), Object.class);
                if (null != responseEntity.getBody()) {
                    return (Double) responseEntity.getBody();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Do something else
            // Return you are not allowed
            return -1.0;
        }
        return -1.0;
    }
}