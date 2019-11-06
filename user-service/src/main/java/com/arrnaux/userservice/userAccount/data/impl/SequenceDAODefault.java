package com.arrnaux.userservice.userAccount.data.impl;

import com.arrnaux.userservice.userAccount.data.SequenceDAO;
import com.arrnaux.userservice.userAccount.model.SequenceId;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@Log4j

public class SequenceDAODefault implements SequenceDAO {
    @Autowired
    private MongoOperations mongoOperation;

    @Override
    public long getNextSequenceId(String key) {

        Query query = new Query(Criteria.where("_id").is(key));

        Update update = new Update();
        update.inc("seq", 1);

        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        SequenceId seqId = mongoOperation.findAndModify(query, update, options, SequenceId.class);

        if (seqId == null) {
            log.error("O eroare");
        }

        return seqId.getSeq();

    }
}
