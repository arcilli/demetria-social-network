package com.arrnaux.friendshiprelationservice.data;

import com.arrnaux.demetria.core.followRelation.model.FollowRelationValidity;
import com.arrnaux.demetria.core.followRelation.model.Person;
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
    OEdge storeValidFollowingRelation(OVertex source, OVertex destination);

    /**
     * @param person
     * @param destination
     * @param followRelationValidity enum (boolean). If it is not passed, then parameter is not used in query.
     * @return the edge between source & destination, in this direction. If a @followRelationValidity parameter is passed,
     * will retrieve only the edge having that value.
     */
    @Nullable
    OEdge findFollowingEdge(Person person, Person destination, FollowRelationValidity... followRelationValidity);

    @Nullable
    OEdge invalidateFollowingEdge(Person source, Person destination);

}
