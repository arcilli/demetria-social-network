package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.model.Person;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "follow")
public class FollowRelationService {

    @Autowired
    FollowRelationDAO followRelationDAO;

    @RequestMapping(value = "{targetUserName}", method = RequestMethod.POST)
    // TODO: return a boolean for operation with/out success.
    public Boolean followUser(@PathVariable("targetUserName") String targetUserName,
                              @RequestBody String sourceUsername) {

        OVertex sourceVertex = followRelationDAO.storePerson(new Person(sourceUsername));
        OVertex targetVertex = followRelationDAO.storePerson(new Person(targetUserName));

        followRelationDAO.storeFollowingRelation(sourceVertex, targetVertex);
        return true;
    }

    /**
     * @param source
     * @param target
     * @return true if userName1 is following username2. Otherwise, return false.
     */
    @RequestMapping(value = "/followCheck/{user1}/{user2}")
    public Boolean checkFollowRelation(@PathVariable("user1") Person source,
                                       @PathVariable("user2") Person target) {
        OEdge edge = followRelationDAO.findFollowingEdge(source, target);
        return null != edge;
    }
}
