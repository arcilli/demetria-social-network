package com.arrnaux.postservice.services;

import com.arrnaux.demetria.core.interaction.FriendshipUtils;
import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.postservice.Helper.UserAsOwnerOperations;
import com.arrnaux.postservice.data.SNPostDAO;
import com.mongodb.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "timeline")
public class TimelineService {

    final
    RestTemplate restTemplate;

    final
    SNPostDAO snPostDAO;

    final
    UserAsOwnerOperations userAsOwnerOperations;

    public TimelineService(SNPostDAO snPostDAO, RestTemplate restTemplate, UserAsOwnerOperations userAsOwnerOperations) {
        this.snPostDAO = snPostDAO;
        this.restTemplate = restTemplate;
        this.userAsOwnerOperations = userAsOwnerOperations;
    }

    /**
     * @param loggedUser
     * @param lastShowedId is equal to SNPost.DEFAULT_POST_ID if no post has been displayed so far.
     * @return
     */
    @RequestMapping(value = "showMorePosts/{lastShowedId}", method = RequestMethod.POST)
    public List<SNPost> showMorePosts(@RequestBody SNUser loggedUser, @PathVariable("lastShowedId") String lastShowedId) {
        List<String> ids = getFollowedPersons(
                GraphPersonEntity.builder()
                        .storedId(loggedUser.getId())
                        .userName(loggedUser.getUserName())
                        .build()
        );
        // Include my own posts in timeline.
        ids.add(loggedUser.getId());

        // Getting all posts that are owned by a person having one of the ids.
        List<SNPost> postsToBeDisplayed = snPostDAO.getMorePostsFromUsers(5, lastShowedId,
                ids, PostVisibility.PUBLIC);

        // <UserId, UserDetails> map
        HashMap<String, SNUser> users = new HashMap<>();
        for (String id : ids) {
            // Make a request to user-service and get an obfuscated user object.
            SNUser snUser = userAsOwnerOperations.requestForSNUser(
                    SNUser.builder()
                            .id(id)
                            .build()
            );
            if (null != snUser) {
                users.put(id, snUser);
            }
        }
        if (null != postsToBeDisplayed) {
            for (SNPost snPost : postsToBeDisplayed) {
                snPost.setOwner(users.get(snPost.getOwnerId()));
                UserAsOwnerOperations.addOwnerToComment(snPost);
            }
            return postsToBeDisplayed;
        }
        return null;
    }

    @RequestMapping(value = "showMorePosts/{lastShowedId}/{userName}", method = RequestMethod.POST)
    public List<SNPost> showMorePostsFromUserName(@RequestBody PostVisibility postVisibility,
                                                  @PathVariable("lastShowedId") String lastShowedId,
                                                  @PathVariable("userName") String userName) {
        SNUser targetUser = userAsOwnerOperations.requestForSNUser(
                SNUser.builder()
                        .userName(userName)
                        .build()
        );

        // Check if the userName is valid.
        if (null == targetUser) {
            return null;
        }

        List<String> ownersIds = new ArrayList<>();
        ownersIds.add(targetUser.getId());

        List<SNPost> postsToBeDisplayed;
        postsToBeDisplayed = getPostsFromUsers(ownersIds, lastShowedId, postVisibility);

        if (null != postsToBeDisplayed) {
            for (SNPost snPost : postsToBeDisplayed) {
                snPost.setOwner(targetUser);
                userAsOwnerOperations.addOwnerToComment(snPost);
            }
        }
        return postsToBeDisplayed;
    }

    /**
     * @param graphPersonEntity
     * @return the ids of followed person from friendship-service.
     */
    @Nullable
    private List<String> getFollowedPersons(GraphPersonEntity graphPersonEntity) {
        return FriendshipUtils.getFollowedUsersIds(restTemplate,
                SNUser.builder()
                        .id(graphPersonEntity.getStoredId())
                        .userName(graphPersonEntity.getUserName())
                        .build()
        );
    }

    @Nullable
    private List<SNPost> getPostsFromUsers(List<String> ids, String lastShowedId, PostVisibility postVisibility) {
        return snPostDAO.getMorePostsFromUsers(5, lastShowedId,
                ids, postVisibility);
    }
}