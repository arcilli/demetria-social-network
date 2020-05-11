package com.arrnaux.frontend.util.friendship;

import com.arrnaux.demetria.core.interaction.BasicFriendshipUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import lombok.extern.java.Log;
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

    public static boolean createPersonVertex(RestTemplate restTemplate, SNUser snUser) {
        return BasicFriendshipUtils.createPersonVertex(restTemplate, snUser);
    }

    public static boolean executeFollowUserRequest(SNUser loggedUser, String userNameToBeFollowed) {
        return BasicFriendshipUtils.followUser(restTemplate, loggedUser, userNameToBeFollowed);
    }

    public static boolean unfollowUser(SNUser loggedUser, String usernameToBeUnfollowed) {
        return BasicFriendshipUtils.unfollowUser(restTemplate, loggedUser, usernameToBeUnfollowed);
    }

    public static int getNoFollowedUsers(SNUser snUser) {
        return BasicFriendshipUtils.getNoFollowedUsers(restTemplate, log, snUser);
    }

    public static int getNoFollowers(SNUser snUser) {
        return BasicFriendshipUtils.getNoFollowers(restTemplate, log, snUser);
    }

    @Nullable
    public static List<SNUser> getFollowedUsers(@Nullable SNUser snUser) {
        return BasicFriendshipUtils.getFollowedPersons(restTemplate, snUser);
    }

    @Nullable
    public static List<SNUser> getFollowers(@Nullable SNUser snUser) {
        return BasicFriendshipUtils.getFollowers(restTemplate, snUser);
    }

    public static boolean checkFollowRelation(SNUser snUser1, SNUser snUser2) {
        return BasicFriendshipUtils.checkFollowRelation(restTemplate, snUser1, snUser2);
    }

    public static boolean deleteUserFromGraph(SNUser snUser) {
        return BasicFriendshipUtils.deletePersonFromGraph(restTemplate, snUser);
    }
}