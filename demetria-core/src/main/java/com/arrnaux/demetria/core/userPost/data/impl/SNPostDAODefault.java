package com.arrnaux.demetria.core.userPost.data.impl;

import com.arrnaux.demetria.core.userAccount.data.SNUserRepository;
import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.data.SNPostRepository;
import com.arrnaux.demetria.core.userPost.model.Comment;
import com.arrnaux.demetria.core.userPost.model.PostVisibility;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import com.arrnaux.demetria.core.userPost.model.Vote;
import com.mongodb.client.MongoClients;
import lombok.extern.log4j.Log4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Log4j
@Repository
public class SNPostDAODefault implements SNPostDAO {

    @Autowired
    private SNPostRepository snPostRepository;

    @Autowired
    private SNUserRepository snUserRepository;

    private MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "test");

    @Override
    @Nullable
    public SNPost getPostById(String postId) {
        return snPostRepository.findById(postId).orElse(null);
    }

    @Override
    @Nullable
    public List<SNPost> getUserPosts(SNUser snUser) {
        log.info("Retrieve posts for user: " + snUser);
        return snPostRepository.findByOwnerId(snUser.getId()).orElse(null);
    }

    @Override
    public SNPost savePost(SNPost snPost) {
        return snPostRepository.save(snPost);
    }

    @Override
    @Nullable
    public List<SNPost> getUserPostsDateDesc(String ownerId) {
        return snPostRepository.findByOwnerIdOrderByCreationDateDesc(ownerId).orElse(null);
    }

    @Override
    public int removePost(String postId) {
        Optional<List<SNPost>> posts = snPostRepository.deleteSNPostById(postId);
        // Should check for a NPE?
        return posts.map(List::size).orElse(-1);
    }

    @Override
    @Nullable
    public List<SNPost> getUserPostsDateDesc(String ownerId, PostVisibility postVisibility) {
        return snPostRepository.findByOwnerIdAndVisibilityOrderByCreationDate(ownerId, postVisibility).orElse(null);
    }

    @Override
    @Nullable
    public SNPost removeVote(Vote vote) {
        final Query query = new Query(new Criteria().andOperator(
                where("_id").is(vote.getPostId())
        ));
        SNPost post = mongoOps.findOne(query, SNPost.class);
        if (null != post) {
            List<Vote> voteList = post.getVoteList();
            if (null != voteList) {
                voteList.removeIf(element -> element.getOwner().equals(vote.getOwner()));
            }
            post.setVoteList(voteList);
        }
        return post;
    }
}