package com.arrnaux.friendshiprelationservice.services;

import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("suggestions")
@RestController
public class UserSuggestion {

    private final FollowRelationDAO followRelationDAO;

    private static final int MAX_POPULAR_SUGGESTIONS = 5;

    public UserSuggestion(FollowRelationDAO followRelationDAO) {
        this.followRelationDAO = followRelationDAO;
    }

    /**
     * @param userId
     * @return a list of ids, corresponding to the suggested users for @userId
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public List<String> getUserSuggestionsFromGraph(@PathVariable("userId") String userId) {
        return followRelationDAO.getSuggestionsFromGraph(
                GraphPersonEntity.builder()
                        .storedId(userId)
                        .build()
        );
    }

    /**
     * @param userId
     * @return @maxNumber of the most popular persons that are not already followed by @userId
     */
    @RequestMapping(value = "popular/user/{userId}")
    public List<String> getMostPopularPersons(@PathVariable("userId") String userId) {
        return followRelationDAO.getMostPopularPersons(
                GraphPersonEntity.builder()
                        .storedId(userId)
                        .build(),
                MAX_POPULAR_SUGGESTIONS
        );
    }
}
