package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.orientechnologies.orient.core.record.OVertex;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("graphOperations")
public class GraphOperationsService {

    final
    FollowRelationDAO followRelationDAO;

    public GraphOperationsService(FollowRelationDAO followRelationDAO) {
        this.followRelationDAO = followRelationDAO;
    }

    @RequestMapping(value = "createVertexRepresentation", method = RequestMethod.POST)
    public boolean createVertexRepresentationForPerson(@RequestBody SNUser snUser) {
        OVertex person = followRelationDAO.storePerson(
                GraphPersonEntity.builder()
                        .userName(snUser.getUserName())
                        .storedId(snUser.getId())
                        .build()
        );
        return null != person;
    }

    @RequestMapping(value = "deletePersonFromGraph", method = RequestMethod.POST)
    public boolean deletePersonAndItsEdges(@RequestBody SNUser userToBeDeleted) {
        return followRelationDAO.deletePersonEntityFromGraph(
                GraphPersonEntity.builder()
                        .userName(userToBeDeleted.getUserName())
                        .storedId(userToBeDeleted.getId())
                        .build()
        );
    }

    @RequestMapping(value = "noFollowedPersons/{userId}", method = RequestMethod.GET)
    public long getNumberOfFollowedPersons(@PathVariable("userId") String userId) {
        if (null != userId) {
            return followRelationDAO.countNumberOfFollowedPersons(
                    GraphPersonEntity.builder()
                            .storedId(userId)
                            .build()
            );
        }
        return -1;
    }

    @RequestMapping(value = "noFollowers/{userId}", method = RequestMethod.GET)
    public long getNumberOfFollowers(@PathVariable("userId") String userId) {
        if (null != userId) {
            return followRelationDAO.countNumberOfFollowers(
                    GraphPersonEntity.builder()
                            .storedId(userId)
                            .build()
            );
        }
        return -1;
    }

    /**
     * @param userId
     * @return a list of user ids that follows the user with @userId.
     */
    @RequestMapping(value = "followedPersons/{userId}", method = RequestMethod.GET)
    public List<String> followedPersons(@PathVariable("userId") String userId) {
        return followRelationDAO.getFollowedUsersIds(
                GraphPersonEntity.builder()
                        .storedId(userId)
                        .build()
        );
    }

    /**
     * @param userId
     * @return the list of followers of @userId
     */
    @RequestMapping(value = "followers/{userId}", method = RequestMethod.GET)
    public List<String> getFollowers(@PathVariable("userId") String userId) {
        return followRelationDAO.getFollowersIds(
                GraphPersonEntity.builder()
                        .storedId(userId)
                        .build()
        );
    }
}
