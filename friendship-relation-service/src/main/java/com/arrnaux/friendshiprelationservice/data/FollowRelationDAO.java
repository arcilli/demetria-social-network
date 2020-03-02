package com.arrnaux.friendshiprelationservice.data;

import com.arrnaux.friendshiprelationservice.model.Person;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import org.springframework.stereotype.Service;

@Service
public interface FollowRelationDAO {
    /**
     * @param person
     * @return
     */
    OVertex storePerson(Person person);

    /**
     * @param source
     * @param destination
     * @return
     */
    OEdge storeFollowingRelation(OVertex source, OVertex destination);

    /**
     * @param source
     * @param destination
     * @return true if exists one oriented edge from source to destination. Otherwise, return false.
     */
    boolean existsFollowingRelationBetweenPeople(Person source, Person destination);
}
