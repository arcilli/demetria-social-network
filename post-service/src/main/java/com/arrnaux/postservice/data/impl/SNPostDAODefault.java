package com.arrnaux.postservice.data.impl;

import com.arrnaux.demetria.core.models.userAccount.SNUser;
import com.arrnaux.demetria.core.models.userPost.Comment;
import com.arrnaux.demetria.core.models.userPost.PostVisibility;
import com.arrnaux.demetria.core.models.userPost.SNPost;
import com.arrnaux.demetria.core.models.userPost.Vote;
import com.arrnaux.postservice.data.SNPostDAO;
import com.arrnaux.postservice.data.SNPostRepository;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.log4j.Log4j;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Log4j
@Repository
public class SNPostDAODefault implements SNPostDAO {

    private final SNPostRepository snPostRepository;

    private MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "test");

    public SNPostDAODefault(SNPostRepository snPostRepository) {
        this.snPostRepository = snPostRepository;
    }

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
    public SNPost removeVoteGivenByUser(String postId, String userId) {
        // TODO: replace this with a mongo update.
        final Query query = new Query(new Criteria().andOperator(
                where("_id").is(postId)
        ));
        SNPost post = mongoOps.findOne(query, SNPost.class);
        if (null != post) {
            List<Vote> voteList = post.getVoteList();
            if (null != voteList) {
                voteList.removeIf(element -> element.getOwnerId().equals(userId));
            }
            post.setVoteList(voteList);
        }
        return post;
    }

    @Override
    @Nullable
    public Comment addCommentToPost(Comment comment, SNPost snPost) {
        comment.setCreationDate(new Date());
        Update update = new Update().addToSet("commentList", comment);
        Query query = new Query(where("_id").is(snPost.getId()));
        UpdateResult result = mongoOps.updateFirst(query, update, SNPost.class);
        if (result.wasAcknowledged()) {
            return comment;
        }
        return null;
    }

    @Override
    public Integer getLastCommentIndexForPost(SNPost snPost) {
        try {
            Aggregation aggregation = newAggregation(
                    match(where("_id").is(snPost.getId()).andOperator(Criteria.where("commentList")).exists(true)),
                    project()
                            .andExclude("_id")
                            .and("commentList")
                            .size()
                            .as("count")
            );

            AggregationResults<Document> results = mongoOps.aggregate(aggregation, SNPost.class, Document.class);
            if (Objects.requireNonNull(results.getUniqueMappedResult()).size() > 0) {
                return (Integer) (Objects.requireNonNull(results.getUniqueMappedResult())).get("count");
            }
        } catch (Exception e) {
            log.trace(e.toString());
        }
        return 0;
    }

    @Nullable
    @Override
    public List<SNPost> getMorePostsFromUsers(Integer nrOfPostToBeRetrieved, String snPostId, List<String> userIds,
                                              PostVisibility postVisibility) {
        // select nrOfPostsToBeRetrieved public posts that have the snPostId timestamp lower than snPostId
        // and that are owned by an user from userIds.

        SNPost currentPost;

        // Find the first post. It will be used to retrieve posts that are older than it.
        if (snPostId.equals("-1")) {
            Optional<SNPost> snPostOptional;
            if (postVisibility == PostVisibility.NONE) {
                snPostOptional = snPostRepository.findFirstByOwnerIdInOrderByCreationDateDesc(userIds);
            } else {
                snPostOptional = snPostRepository.
                        findFirstByOwnerIdInAndVisibilityOrderByCreationDateDesc(userIds, postVisibility);
            }
            currentPost = snPostOptional.orElse(null);
        } else {
            currentPost = getPostById(snPostId);
        }
        if (null != currentPost) {
            Date currentPostDate = currentPost.getCreationDate();
            Query query = new Query()
                    .addCriteria(Criteria.where("creationDate").lte(currentPostDate))
                    .addCriteria(Criteria.where("ownerId").in(userIds))
                    .limit(nrOfPostToBeRetrieved)
                    .with(Sort.by(Sort.Direction.DESC, "creationDate"));

            if (postVisibility != PostVisibility.NONE) {
                // The postVisibility matters, so taking in consideration when the query is made.
                query.addCriteria(Criteria.where("visibility").is(postVisibility));
            }
            return mongoOps.find(query, SNPost.class);
        }
        return null;
    }
}
