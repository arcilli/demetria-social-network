package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.demetria.core.models.followRelation.FollowRelationValidity;
import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "follow")
public class FollowService {

    final
    FollowRelationDAO followRelationDAO;

    final
    RestTemplate restTemplate;

    public FollowService(FollowRelationDAO followRelationDAO, RestTemplate restTemplate) {
        this.followRelationDAO = followRelationDAO;
        this.restTemplate = restTemplate;
    }

    @RequestMapping(value = "{targetUserName}", method = RequestMethod.POST)
    // TODO: return a boolean for operation with/without success.
    public Boolean followUser(@PathVariable("targetUserName") String targetUserName,
                              @RequestBody String sourceUsername) {

        // TODO: refactor this duplicate code.
        OVertex sourceVertex = null, targetVertex = null;
        // Retrieve the id for the userNames.
        String target = "http://user-service/users/info/" + sourceUsername;
        ResponseEntity<SNUser> responseEntity = restTemplate.exchange(target, HttpMethod.POST, HttpEntity.EMPTY, SNUser.class);
        if (null != responseEntity.getBody()) {
            SNUser user = responseEntity.getBody();
            sourceVertex = followRelationDAO.storePerson(new GraphPersonEntity(user));
        }
        target = "http://user-service/users/info/" + targetUserName;
        responseEntity = restTemplate.exchange(target, HttpMethod.POST, HttpEntity.EMPTY, SNUser.class);
        if (null != responseEntity.getBody()) {
            SNUser user = responseEntity.getBody();
            targetVertex = followRelationDAO.storePerson(
                    new GraphPersonEntity(user));
        }
        if (null != sourceVertex && null != targetVertex) {
            followRelationDAO.storeValidFollowingRelation(sourceVertex, targetVertex);
            return true;
        }
        return false;
    }

    /**
     * @param source
     * @param target
     * @return true if a valid edge exist between user1 (source) and user2(target) and if the edge is a valid relation.
     */
    @RequestMapping(value = "/check/{user1}/{user2}", method = RequestMethod.GET)
    public Boolean checkFollowRelation(@PathVariable("user1") String source,
                                       @PathVariable("user2") String target) {
        OEdge edge = followRelationDAO.findFollowingEdge(
                GraphPersonEntity.builder().userName(source).build(),
                GraphPersonEntity.builder().userName(target).build(),
                FollowRelationValidity.VALID);
        return null != edge;
    }
}
