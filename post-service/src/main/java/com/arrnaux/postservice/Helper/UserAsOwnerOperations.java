package com.arrnaux.postservice.Helper;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.demetria.core.models.userPost.Vote;
import com.arrnaux.postservice.util.UserUtilsService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class UserAsOwnerOperations {

    public static void addOwnerToComment(@NotNull SNPost snPost) {
        List<Comment> comments = snPost.getCommentList();
        if (null != comments) {
            for (Iterator<Comment> commentIterator = comments.iterator(); commentIterator.hasNext(); ) {
                Comment comment = commentIterator.next();
                SNUser commentOwner = UserUtilsService.getObfuscatedUserById(comment.getOwnerId());
                if (null != commentOwner) {
                    comment.setOwner(commentOwner);
                } else {
                    commentIterator.remove();
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
     */
    public static void extractUserVoteFromPost(@Nullable String userId, SNPost post) {
        if (null != userId) {
            List<Vote> votes = post.getVoteList();
            if (null != votes) {
                List<Vote> singularVote = new ArrayList<>();
                for (Iterator<Vote> voteIterator = votes.iterator(); voteIterator.hasNext(); ) {
                    Vote currentVote = voteIterator.next();
                    if (null != currentVote) {
                        if (userId.equals(currentVote.getOwnerId())) {
                            singularVote.add(currentVote);
                        } else {
                            // Delete the vote if the user does not exist anymore.
                            if (null == UserUtilsService.getObfuscatedUserById(currentVote.getOwnerId())) {
                                voteIterator.remove();
                            }
                        }
                    }
                }
                if (1 == singularVote.size()) {
                    post.setVoteList(singularVote);
                }
            }
        }
    }
}