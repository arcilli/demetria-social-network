//package com.arrnaux.userservice.userAccount.data;
//
//import com.arrnaux.userservice.userAccount.model.SNUser;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Service;
//
//import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
//import static org.springframework.data.mongodb.core.query.Criteria.where;
//import static org.springframework.data.mongodb.core.query.Query.query;
//
//@Service
//
//@NoArgsConstructor
//public class NextSequenceService {
//    @Autowired
//    private SNUserRepository snUserRepository;
//
//    public int getNextSequence(String seqName) {
//        SNUser counter = snUserRepository.findAndModify(
//                query(where("_id").is(seqName)),
//                new Update().inc("seq", 1),
//                options().returnNew(true).upsert(true),
//                SNUser.class);
//        return counter.getSeq();
//    }
//}
