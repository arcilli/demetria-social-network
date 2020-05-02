package com.arrnaux.postservice.Helper;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.postservice.util.UserUtilsService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

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
}
