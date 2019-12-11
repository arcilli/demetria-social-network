package com.arrnaux.demetria.core.userPost.data;

import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.SNPost;

public interface SNPostCustomRepository {

    // returns the id of the inserted comment
    String saveCommentForPost(SNPost snPost, Comment comment);
}
