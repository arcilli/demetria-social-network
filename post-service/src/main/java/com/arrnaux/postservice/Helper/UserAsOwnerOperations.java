package com.arrnaux.postservice.Helper;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class UserAsOwnerOperations {

    final
    RestTemplate restTemplate;

    public UserAsOwnerOperations(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void addOwnerToComment(@NotNull SNPost snPost) {
        List<Comment> comments = snPost.getCommentList();
        if (null != comments) {
            for (Comment comment : comments) {
                SNUser commentOwner = requestForSNUser
                        (SNUser.builder()
                                .id(comment.getOwnerId())
                                .build()
                        );
                if (null != commentOwner) {
                    comment.setOwner(commentOwner);
                }
            }
        }
    }

    /**
     * @param snUser is not completely populated. It contains only id or userName.
     * @return the user with obfuscated information. The obfuscation is made at the user service.
     */
    @Nullable
    public SNUser requestForSNUser(SNUser snUser) {
        String identifier = snUser.getUserName();
        StringBuilder targetURL = new StringBuilder("http://user-service/users/info/");
        if (null != snUser.getUserName()) {
            identifier = snUser.getUserName();
            targetURL.append("username");
        } else if (null != snUser.getId()) {
            identifier = snUser.getId();
            targetURL.append("id");
        }
        ResponseEntity<SNUser> responseEntity = restTemplate.exchange(targetURL.toString(), HttpMethod.POST,
                new HttpEntity<>(identifier), SNUser.class);
        return responseEntity.getBody();
    }
}
