package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("graphOperations")
public class GraphOperationsService {

    final
    FollowRelationDAO followRelationDAO;

    public GraphOperationsService(FollowRelationDAO followRelationDAO) {
        this.followRelationDAO = followRelationDAO;
    }

    @RequestMapping(value = "findFollowedPers", method = RequestMethod.POST)
    public List<String> findFollowedPersons(@RequestBody SNUser loggedUser) {
        return followRelationDAO.getFollowedUsers(
                GraphPersonEntity.builder()
                        .userName(loggedUser.getUserName())
                        .storedId(loggedUser.getId())
                        .build()
        );
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
}
