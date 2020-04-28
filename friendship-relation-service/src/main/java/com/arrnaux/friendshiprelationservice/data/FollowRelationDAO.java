package com.arrnaux.friendshiprelationservice.data;

import com.arrnaux.demetria.core.models.followRelation.FollowRelationValidity;
import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FollowRelationDAO {
    /**
     * @param graphPersonEntity
     * @return
     */
    OVertex storePerson(GraphPersonEntity graphPersonEntity);

    /**
     * @param graphPersonEntity
     * @return a vertex representing the person stored in DB or null if no entry was found.
     */
    @Nullable
    OVertex findByUserName(GraphPersonEntity graphPersonEntity) throws NullArgumentException;

    /**
     * @param source
     * @param destination
     * @return
     */
    OEdge storeValidFollowingRelation(OVertex source, OVertex destination);

    /**
     * @param graphPersonEntity
     * @param destination
     * @param followRelationValidity enum (boolean). If it is not passed, then parameter is not used in query.
     * @return the edge between source & destination, in this direction. If a @followRelationValidity parameter is passed,
     * will retrieve only the edge having that value.
     */
    @Nullable
    OEdge findFollowingEdge(GraphPersonEntity graphPersonEntity, GraphPersonEntity destination, FollowRelationValidity... followRelationValidity);

    @Nullable
    OEdge invalidateFollowingEdge(GraphPersonEntity source, GraphPersonEntity destination);

    /**
     * @param snUser is an incomplete object.
     * @return a list o ids corresponding to the user that @snUser is following.
     */
    @Nullable
    List<String> getFollowedUsers(GraphPersonEntity snUser);

    /**
     * Delete the vertex represented by a GraphPersonalityEntity and its vertices (in & out).
     *
     * @param snUser is a partial object.
     * @return true if the vertex was successfully deleted.
     */
    boolean deletePersonEntityFromGraph(GraphPersonEntity snUser);
}
