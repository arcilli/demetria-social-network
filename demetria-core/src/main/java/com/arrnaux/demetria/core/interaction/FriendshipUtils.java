package com.arrnaux.demetria.core.interaction;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class FriendshipUtils {

    @Nullable
    public static List<SNUser> getFollowedPersons(RestTemplate restTemplate, @Nullable SNUser snUser) {
        List<String> userIds = getFollowedUsersIds(restTemplate, snUser);
        if (null != userIds) {
            List<SNUser> users = new ArrayList<>();
            for (String userId : userIds) {
                // Make a request.
                SNUser user = UserUtils.getObfuscatedUserById(restTemplate, userId);
                users.add(user);
            }
            return users;
        }
        return null;
    }

    @Nullable
    public static List<SNUser> getFollowers(RestTemplate restTemplate, @Nullable SNUser snUser) {
        if (null != snUser && null != snUser.getId()) {
            String targetUrl = "http://friendship-relation-service/graphOperations/noFollowers/" + snUser.getId();
            ResponseEntity<List<SNUser>> responseEntity = (restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, new ParameterizedTypeReference<List<SNUser>>() {
                    }));
            return responseEntity.getBody();
        }
        return null;
    }

    @Nullable
    public static List<String> getFollowedUsersIds(RestTemplate restTemplate, @Nullable SNUser snUser) {
        if (null != snUser) {
            String targetUrl = "http://friendship-relation-service/graphOperations/followedPersons/" + snUser.getId();
            ResponseEntity<List<String>> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<String>>() {
                    });
            return responseEntity.getBody();
        }
        return null;
    }
}
