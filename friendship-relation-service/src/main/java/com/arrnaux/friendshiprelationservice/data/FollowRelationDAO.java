package com.arrnaux.friendshiprelationservice.data;

import com.arrnaux.friendshiprelationservice.model.Person;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public interface FollowRelationDAO {
    /**
     * @param person
     * @return
     */
    OVertex storePerson(Person person);

    /**
     * @param person
     * @return a vertex representing the person stored in DB or null if no entry was found.
     */
    @Nullable
    OVertex findByUserName(Person person) throws NullArgumentException;

    /**
     * @param source
     * @param destination
     * @return
     */
    OEdge storeFollowingRelation(OVertex source, OVertex destination);

    /**
     * @param source
     * @param destination
     * @return
     */
    @Nullable
    OEdge findFollowingEdge(Person source, Person destination);

}
