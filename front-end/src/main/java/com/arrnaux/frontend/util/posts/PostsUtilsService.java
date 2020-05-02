package com.arrnaux.frontend.util.posts;

import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Service
public class PostsUtilsService {
    private static RestTemplate restTemplate;

    private final RestTemplate autowiredComponent;

    public PostsUtilsService(RestTemplate autowiredComponent) {
        this.autowiredComponent = autowiredComponent;
    }

    @PostConstruct
    private void init() {
        restTemplate = this.autowiredComponent;
    }

    public static void createPost(SNPost post) {
        restTemplate.exchange("http://post-service/posts/savePost", HttpMethod.POST,
                new HttpEntity<>(post), String.class);
    }

    public static ResponseEntity<Comment> createComment(SNPost post) {
        String targetUrl = "http://post-service/posts/createComment";
        return restTemplate.exchange(targetUrl, HttpMethod.POST,
                new HttpEntity<>(post), Comment.class);
    }
}
