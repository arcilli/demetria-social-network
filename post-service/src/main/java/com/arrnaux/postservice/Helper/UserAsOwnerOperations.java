package com.arrnaux.postservice.Helper;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.demetria.core.models.userPost.Vote;
import com.arrnaux.postservice.util.UserUtilsService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAsOwnerOperations {

    public static void addOwnerToComment(@NotNull SNPost snPost) {
        List<Comment> comments = snPost.getCommentList();
        if (null != comments) {
            for (Comment comment : comments) {
                SNUser commentOwner = UserUtilsService.getObfuscatedUserById(comment.getOwnerId());
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
        if (null != snUser.getId()) {
            return UserUtilsService.getObfuscatedUserById(snUser.getId());
        } else if (null != snUser.getUserName()) {
            return UserUtilsService.getObfuscatedUserByUserName(snUser.getUserName());
        }
        return null;
    }

    /**
     * @param userId
     * @param post
     * @return the post containing only logged user's vote (if he voted the post) or null if the user has not voted or
     * is not logged.
     */
    public static SNPost extractUserVoteFromPost(@Nullable String userId, SNPost post) {
        if (null != userId) {
            List<Vote> votes = post.getVoteList();
            if (null != votes) {
                List<Vote> singularVote = votes.stream()
                        .filter(k -> k.getOwnerId().equals(userId))
                        .collect(Collectors.toList());
                if (0 != singularVote.size()) {
                    post.setVoteList(singularVote);
                }
            }
        }
        return post;
    }
}