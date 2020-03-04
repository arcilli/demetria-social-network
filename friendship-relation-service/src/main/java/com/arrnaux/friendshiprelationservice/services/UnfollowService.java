package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.model.Person;
import com.orientechnologies.orient.core.record.OEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("unfollow")
public class UnfollowService {

    @Autowired
    FollowRelationDAO followRelationDAO;

    @RequestMapping(value = "{usernameToBeUnfollowed}", method = RequestMethod.POST)
    public Boolean unfollowUser(@PathVariable("usernameToBeUnfollowed") String usernameToBeUnfollowed,
                                @RequestBody String loggedUsername) {
        OEdge edge = followRelationDAO.invalidateFollowingEdge(new Person(loggedUsername),
                new Person(usernameToBeUnfollowed));
        return null != edge;
    }
}
