package com.arrnaux.friendshiprelationservice.data;

import com.arrnaux.friendshiprelationservice.model.Person;
import com.orientechnologies.orient.core.record.OVertex;
import org.springframework.stereotype.Service;

@Service
public interface FollowRelationDAO {
    OVertex storePerson(Person person);

    void storeFollowingRelation(OVertex source, OVertex destination);
}
