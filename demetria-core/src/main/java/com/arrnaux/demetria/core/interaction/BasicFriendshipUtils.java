package com.arrnaux.demetria.core.interaction;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BasicFriendshipUtils {
    private static final String serviceBaseUrl = "http://friendship-relation:8082/";

    public static boolean createPersonVertex(RestTemplate restTemplate, @Nullable SNUser snUser) {
        if (null != snUser && null != snUser.getUserName() && null != snUser.getId()) {
            String targetUrl = serviceBaseUrl + "graphOperations/createVertexRepresentation";
            try {
                ResponseEntity<Boolean> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.POST,
                        new HttpEntity<>(snUser), Boolean.class);
                return (null != responseEntity.getBody()) && (responseEntity.getBody());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Nullable
    public static List<SNUser> getFollowedPersons(RestTemplate restTemplate, @Nullable SNUser snUser) {
        List<String> userIds = getFollowedPersonsIds(restTemplate, snUser);
        if (null != userIds) {
            return getObfuscatedUsersForIds(restTemplate, userIds);
        }
        return null;
    }

    @Nullable
    public static List<SNUser> getFollowers(RestTemplate restTemplate, @Nullable SNUser snUser) {
        List<String> userIds = getFollowersIds(restTemplate, snUser);
        if (null != userIds) {
            return getObfuscatedUsersForIds(restTemplate, userIds);
        }
        return null;
    }

    public static boolean followUser(RestTemplate restTemplate, SNUser loggedUser, String userNameToBeFollowed) {
        String targetUrl = serviceBaseUrl + "follow/" + userNameToBeFollowed;
        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(loggedUser.getUserName()),
                        Boolean.class);
        if (null != responseEntity.getBody()) {
            return responseEntity.getBody();
        }

        return false;
    }

    public static boolean unfollowUser(RestTemplate restTemplate, SNUser loggedUser, String usernameToBeUnfollowed) {
        String targetUrl = serviceBaseUrl + "unfollow/" + usernameToBeUnfollowed;
        ResponseEntity<Boolean> responseEntity =
                restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(loggedUser.getUserName()),
                        Boolean.class);
        if (null != responseEntity.getBody()) {
            return responseEntity.getBody();
        }
        return false;
    }

    public static int getNoFollowedUsers(RestTemplate restTemplate, Logger log, SNUser snUser) {
        if (null == snUser || null == snUser.getId()) {
            return -1;
        }

        try {
            String targetUrl = serviceBaseUrl + "graphOperations/noFollowedPersons/" + snUser.getId();
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

    public static int getNoFollowers(RestTemplate restTemplate, Logger log, SNUser snUser) {
        if (null == snUser || null == snUser.getId()) {
            return -1;
        }
        try {
            String targetUrl = serviceBaseUrl + "graphOperations/noFollowers/" + snUser.getId();
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

    /**
     * @param restTemplate
     * @param snUser1
     * @param snUser2
     * @return true if @snUser1 is following snUser2
     */
    public static boolean checkFollowRelation(RestTemplate restTemplate, SNUser snUser1, SNUser snUser2) {
        String targetUrl = serviceBaseUrl + "follow/check/" + snUser1.getUserName() + "/" + snUser2.getUserName();
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, HttpEntity.EMPTY,
                Boolean.class);
        if (null != responseEntity.getBody()) {
            return responseEntity.getBody();
        }
        return false;
    }

    public static boolean deletePersonFromGraph(RestTemplate restTemplate, SNUser snUser) {
        String targetURL = serviceBaseUrl + "graphOperations/deletePersonFromGraph";
        ResponseEntity<Boolean> responseEntity = restTemplate.exchange(targetURL, HttpMethod.POST,
                new HttpEntity<>(snUser), Boolean.class);
        if (null != responseEntity.getBody() && responseEntity.getBody()) {
            return true;
        }
        return false;
    }

    /**
     * Used for building the timeline.
     *
     * @param restTemplate
     * @param snUser
     * @return
     */
    @Nullable
    public static List<String> getFollowedPersonsIds(RestTemplate restTemplate, @Nullable SNUser snUser) {
        if (null != snUser && null != snUser.getId()) {
            String targetUrl = serviceBaseUrl + "graphOperations/followedPersons/" + snUser.getId();
            ResponseEntity<List<String>> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, new ParameterizedTypeReference<List<String>>() {
                    });
            return responseEntity.getBody();
        }
        return null;
    }

    @Nullable
    private static List<String> getFollowersIds(RestTemplate restTemplate, @Nullable SNUser snUser) {
        if (null != snUser && null != snUser.getId()) {
            String targetUrl = serviceBaseUrl + "graphOperations/followers/" + snUser.getId();
            ResponseEntity<List<String>> responseEntity = (restTemplate.exchange(targetUrl, HttpMethod.GET,
                    HttpEntity.EMPTY, new ParameterizedTypeReference<List<String>>() {
                    }));
            return responseEntity.getBody();
        }
        return null;
    }

    @Nullable
    private static List<SNUser> getObfuscatedUsersForIds(RestTemplate restTemplate, List<String> userIds) {
        if (null != userIds) {
            return userIds.stream()
                    .map(id -> BasicUserUtils.getObfuscatedUserById(restTemplate, id))
                    .collect(Collectors.toList());
        }
        return null;
    }
}
