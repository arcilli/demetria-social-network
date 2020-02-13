package com.arrnaux.demetria.core.userPost.data.impl;

import com.arrnaux.demetria.core.userAccount.model.SNUser;
import com.arrnaux.demetria.core.userPost.data.SNPostDAO;
import com.arrnaux.demetria.core.userPost.data.SNPostRepository;
import com.arrnaux.demetria.core.userPost.model.PostVisibility;
import com.arrnaux.demetria.core.userPost.model.SNPost;
import com.arrnaux.demetria.core.userPost.model.Vote;
import com.mongodb.client.MongoClients;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Log4j
@Repository
public class SNPostDAODefault implements SNPostDAO {

    @Autowired
    private SNPostRepository snPostRepository;

    private MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "test");

    @Override
    public SNPost getPostById(String postId) {
        return snPostRepository.findById(postId).orElse(null);
    }

    @Override
    public List<SNPost> getUserPosts(SNUser snUser) {
        log.info("Retrieve posts for user: " + snUser);
        return snPostRepository.findByOwnerId(snUser.getId());
    }

    @Override
    public SNPost savePost(SNPost snPost) {
        return snPostRepository.save(snPost);
    }

    @Override
    public List<SNPost> getUserPostsDateDesc(String id) {
        return snPostRepository.findByOwnerIdOrderByCreationDateDesc(id);
    }

    @Override
    public int removePost(String postId) {
        List<SNPost> postsToBeDeleted = snPostRepository.deleteSNPostById(postId);
        return postsToBeDeleted.size();
    }

    @Override
    public List<SNPost> getUserPostsDescending(String userName, PostVisibility postVisibility) {
        return snPostRepository.findByOwnerUserNameAndVisibilityOrderByCreationDate(userName, postVisibility);
    }

    @Override
    public SNPost removeVote(Vote vote) {
        final Query query = new Query(new Criteria().andOperator(
                where("_id").is(vote.getPostId())
        ));
        SNPost post = mongoOps.findOne(query, SNPost.class);
        if (null != post) {
            List<Vote> voteList = post.getVoteList();
            if (null != voteList) {
                voteList.removeIf(element -> element.getOwnerId().equals(vote.getOwnerId()));
            }
            post.setVoteList(voteList);
        }
        return post;
    }

    @Override
    public double getPostRank(SNPost post) {
        // TODO: replace this with a DB-side logic
        SNPost actualPost = getPostById(post.getId());
        if (null != actualPost) {

        }
        return -1;
    }
}