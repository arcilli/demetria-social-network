package com.arrnaux.demetria.core.interaction;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.demetria.core.models.userPost.Vote;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BasicPostsUtils {
    private static final String serviceBaseUrl = "http://post-service:8083/";

    public static void createPost(RestTemplate restTemplate, SNPost post) {
        restTemplate.exchange(serviceBaseUrl + "posts/savePost", HttpMethod.POST,
                new HttpEntity<>(post), String.class);
    }

    public static ResponseEntity<SNPost> getPost(RestTemplate restTemplate, String postId) {
        String requestURL = serviceBaseUrl + "posts/id/" + postId;
        return restTemplate.exchange(requestURL, HttpMethod.GET, HttpEntity.EMPTY,
                SNPost.class);
    }

    public static ResponseEntity<Boolean> deletePost(RestTemplate restTemplate, SNPost post) {
        String url = serviceBaseUrl + "posts/deletePost/";
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(post), Boolean.class);
    }

    public static ResponseEntity<Comment> createComment(RestTemplate restTemplate, SNPost post) {
        String targetUrl = serviceBaseUrl + "posts/createComment";
        return restTemplate.exchange(targetUrl, HttpMethod.POST,
                new HttpEntity<>(post), Comment.class);
    }

    public static List<SNPost> getPostsForUser(RestTemplate restTemplate, String lastShowedId, SNUser user) {
        String targetUrl = serviceBaseUrl + "timeline/showMorePosts/" + lastShowedId;
        return restTemplate.exchange(targetUrl, HttpMethod.POST,
                new HttpEntity<>(user), new ParameterizedTypeReference<List<SNPost>>() {
                }).getBody();
    }

    public static List<SNPost> getPostsFromUserFromUrl(RestTemplate restTemplate, String lastShowedId, String userName,
                                                       PostVisibility postVisibility) {
        String targetUrl = serviceBaseUrl + "timeline/showMorePosts/" + lastShowedId + "/" + userName;
        return restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(postVisibility),
                new ParameterizedTypeReference<List<SNPost>>() {
                }).getBody();
    }

    public static Double voteAPost(RestTemplate restTemplate, Vote vote) {
        String targetUrl = serviceBaseUrl + "posts/vote/";
        ResponseEntity<Double> voteRankValue = restTemplate.exchange(targetUrl, HttpMethod.POST, new HttpEntity<>(vote),
                Double.class);
        if (null != voteRankValue.getBody()) {
            return voteRankValue.getBody();
        }
        return -1.0;
    }
}
