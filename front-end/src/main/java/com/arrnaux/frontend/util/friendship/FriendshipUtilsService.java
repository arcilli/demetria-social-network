package com.arrnaux.frontend.util.friendship;

import com.arrnaux.demetria.core.interaction.FriendshipUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import lombok.extern.java.Log;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@Log
public class FriendshipUtilsService {
    private static RestTemplate restTemplate;

    private final RestTemplate autowiredComponent;

    public FriendshipUtilsService(RestTemplate autowiredComponent) {
        this.autowiredComponent = autowiredComponent;
    }

    @PostConstruct
    private void init() {
        restTemplate = this.autowiredComponent;
    }

    public static boolean followUser(SNUser loggedUser, String userNameToBeFollowed) {
        String targetUrl = "http://friendship-relation-service/follow/" + userNameToBeFollowed;
        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(loggedUser.getUserName()),
                        Boolean.class);
        if (null != responseEntity.getBody()) {
            return responseEntity.getBody();
        }
        return false;
    }

    public static boolean unfollowUser(SNUser loggedUser, String usernameToBeUnfollowed) {
        String targetUrl = "http://friendship-relation-service/unfollow/" + usernameToBeUnfollowed;
        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(loggedUser.getUserName()),
                        Boolean.class);
        if (null != responseEntity.getBody()) {
            return responseEntity.getBody();
        }
        return false;
    }

    public static int getNoFollowedUsers(SNUser snUser) {
        if (null == snUser || null == snUser.getId()) {
            return -1;
        }

        try {
            String targetUrl = "http://friendship-relation-service/graphOperations/noFollowedPersons/" + snUser.getId();
            ResponseEntity<Integer> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, Integer.class);
            if (null != responseEntity.getBody()) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            log.severe(e.toString());
        }
        return -1;
    }

    public static int getNoFollowers(SNUser snUser) {
        if (null == snUser || null == snUser.getId()) {
            return -1;
        }
        try {
            String targetUrl = "http://friendship-relation-service/graphOperations/noFollowers/" + snUser.getId();
            ResponseEntity<Integer> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, Integer.class);
            if (null != responseEntity.getBody()) {
                return responseEntity.getBody();
            }
        } catch (Exception e) {
            log.severe(e.toString());
        }
        return -1;
    }

    @Nullable
    public static List<SNUser> findFollowedUsers(@Nullable SNUser snUser) {
        return FriendshipUtils.getFollowedPersons(restTemplate, snUser);
    }

    @Nullable
    public static List<SNUser> findFollowersUsers(@Nullable SNUser snUser) {
        return FriendshipUtils.getFollowers(restTemplate, snUser);
    }

}