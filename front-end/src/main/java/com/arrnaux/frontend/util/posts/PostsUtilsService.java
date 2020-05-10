package com.arrnaux.frontend.util.posts;

import com.arrnaux.demetria.core.interaction.BasicPostsUtils;
import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.demetria.core.models.userPost.Vote;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;

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
        BasicPostsUtils.createPost(restTemplate, post);
    }

    public static ResponseEntity<Comment> createComment(SNPost post) {
        return BasicPostsUtils.createComment(restTemplate, post);
    }

    public static ResponseEntity<Boolean> deletePost(SNPost post) {
        return BasicPostsUtils.deletePost(restTemplate, post);
    }

    public static ResponseEntity<SNPost> displayPost(String postId) {
        return BasicPostsUtils.getPost(restTemplate, postId);
    }

    public static List<SNPost> getPostsForUser(String lastShowedId, SNUser user) {
        return BasicPostsUtils.getPostsForUser(restTemplate, lastShowedId, user);
    }

    public static List<SNPost> getPostsFromUserFromUrl(String lastShowedId, String userName,
                                                       PostVisibility postVisibility) {
        return BasicPostsUtils.getPostsFromUserFromUrl(restTemplate, lastShowedId, userName, postVisibility);
    }

    public static Double voteAPost(Vote vote) {
        return BasicPostsUtils.voteAPost(restTemplate, vote);
    }
}
