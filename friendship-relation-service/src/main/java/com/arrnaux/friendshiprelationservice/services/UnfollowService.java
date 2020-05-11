package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("unfollow")
public class UnfollowService {

    final
    FollowRelationDAO followRelationDAO;

    public UnfollowService(FollowRelationDAO followRelationDAO) {
        this.followRelationDAO = followRelationDAO;
    }

    @RequestMapping(value = "{usernameToBeUnfollowed}", method = RequestMethod.POST)
    public Boolean unfollowUser(@PathVariable("usernameToBeUnfollowed") String usernameToBeUnfollowed,
                                @RequestBody String loggedUsername) {
        return followRelationDAO.deleteFollowingEdge(
                GraphPersonEntity.builder().userName(loggedUsername).build(),
                GraphPersonEntity.builder().userName(usernameToBeUnfollowed).build());
    }
}
