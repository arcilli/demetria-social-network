package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.util.user.UserUtilsService;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import org.apache.commons.lang.NullArgumentException;
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
    public Boolean followUser(@PathVariable("targetUserName") String targetUserName,
                              @RequestBody String sourceUsername) {

        OVertex sourceVertex = null, targetVertex = null;
        // Retrieve the id for the userNames.
        SNUser sourceUser = UserUtilsService.getObfuscatedUserByUserName(sourceUsername);
        SNUser targetUser = UserUtilsService.getObfuscatedUserByUserName(targetUserName);
        if (null != sourceUser && null != targetUser) {
            sourceVertex = followRelationDAO.storePerson(new GraphPersonEntity(sourceUser));
            targetVertex = followRelationDAO.storePerson(new GraphPersonEntity(targetUser));
        }
        if (null != sourceVertex && null != targetVertex) {
            return null != followRelationDAO.storeFollowsEdge(sourceVertex, targetVertex);
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
        try {
            OEdge edge = followRelationDAO.findFollowingEdge(
                    GraphPersonEntity.builder().userName(source).build(),
                    GraphPersonEntity.builder().userName(target).build());
            return null != edge;
        } catch (NullArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
