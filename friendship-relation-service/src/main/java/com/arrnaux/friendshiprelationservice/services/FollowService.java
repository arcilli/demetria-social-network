package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.demetria.core.followRelation.model.FollowRelationValidity;
import com.arrnaux.demetria.core.followRelation.model.Person;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "follow")
public class FollowService {

    @Autowired
    FollowRelationDAO followRelationDAO;

    @RequestMapping(value = "{targetUserName}", method = RequestMethod.POST)
    // TODO: return a boolean for operation with/without success.
    public Boolean followUser(@PathVariable("targetUserName") String targetUserName,
                              @RequestBody String sourceUsername) {

        OVertex sourceVertex = followRelationDAO.storePerson(new Person(sourceUsername));
        OVertex targetVertex = followRelationDAO.storePerson(new Person(targetUserName));

        followRelationDAO.storeValidFollowingRelation(sourceVertex, targetVertex);
        return true;
    }

    /**
     * @param source
     * @param target
     * @return true if a valid edge exist between user1 (source) and user2(target) and if the edge is a valid relation.
     */
    @RequestMapping(value = "/check/{user1}/{user2}", method = RequestMethod.GET)
    public Boolean checkFollowRelation(@PathVariable("user1") Person source,
                                       @PathVariable("user2") Person target) {
        OEdge edge = followRelationDAO.findFollowingEdge(source, target, FollowRelationValidity.VALID);
        return null != edge;
    }
}
