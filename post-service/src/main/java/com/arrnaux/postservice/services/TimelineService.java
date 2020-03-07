package com.arrnaux.postservice.services;

import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.postservice.Helper.OwnersInteroperability;
import com.arrnaux.postservice.data.SNPostDAO;
import com.mongodb.lang.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    OwnersInteroperability ownersInteroperability;

    public TimelineService(SNPostDAO snPostDAO, RestTemplate restTemplate, OwnersInteroperability ownersInteroperability) {
        this.snPostDAO = snPostDAO;
        this.restTemplate = restTemplate;
        this.ownersInteroperability = ownersInteroperability;
    }

    /**
     * @param loggedUser
     * @param lastShowedId is equal to -1 if no post has been displayed so far.
     * @return
     */
    @RequestMapping(value = "showMorePosts/{lastShowedId}", method = RequestMethod.POST)
    private List<SNPost> showMorePosts(@RequestBody SNUser loggedUser, @PathVariable("lastShowedId") String lastShowedId) {
        List<String> ids = getFollowedPersons(
                GraphPersonEntity.builder()
                        .storedId(loggedUser.getId())
                        .userName(loggedUser.getUserName())
                        .build()
        );
        // Getting all posts that are owned by person having one of the ids.
        List<SNPost> postsToBeDisplayed = snPostDAO.getMorePostsFromUsers(10, lastShowedId,
                ids, PostVisibility.PUBLIC);

        // <UserId, UserDetails> map
        HashMap<String, SNUser> users = new HashMap<>();
        for (String id : ids) {
            // Make a request to user-service and get an obfuscated user object.
            SNUser snUser = getUserDetails(id);
            if (null != snUser) {
                users.put(id, snUser);
            }
        }
        if (null != postsToBeDisplayed) {
            for (SNPost snPost : postsToBeDisplayed) {
                snPost.setOwner(users.get(snPost.getOwnerId()));
                ownersInteroperability.addOwnerToComment(snPost);
            }
            return postsToBeDisplayed;
        }
        return null;
    }

    /**
     * @param graphPersonEntity
     * @return the ids of followed person from friendship-service.
     */
    @Nullable
    private List<String> getFollowedPersons(GraphPersonEntity graphPersonEntity) {
        String targetURL = "http://friendship-relation-service/graphOperations/findFollowedPers";
        SNUser snUser = SNUser.builder()
                .id(graphPersonEntity.getStoredId())
                .userName(graphPersonEntity.getUserName())
                .build();
        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(targetURL, HttpMethod.POST,
                new HttpEntity<>(snUser), new ParameterizedTypeReference<List<String>>() {
                });
        return responseEntity.getBody();
    }

    @Nullable
    private SNUser getUserDetails(String userId) {
        String targetURL = "http://user-service/users/info/id/";
        ResponseEntity<SNUser> responseEntity = restTemplate.exchange(targetURL, HttpMethod.POST, new HttpEntity<>(userId),
                SNUser.class);
        return responseEntity.getBody();
    }
}
