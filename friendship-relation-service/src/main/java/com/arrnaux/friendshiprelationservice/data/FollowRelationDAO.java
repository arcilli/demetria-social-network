package com.arrnaux.friendshiprelationservice.data;

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
     * @return a stored vertex representing the graphPersonEntity.
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
     * @return the edge representing the follow relation.
     */
    @Nullable
    OEdge storeFollowsEdge(OVertex source, OVertex destination);

    /**
     * @param graphPersonEntity
     * @param destination
     * @return the edge between source & destination, in this direction.
     */
    @Nullable
    OEdge findFollowingEdge(GraphPersonEntity graphPersonEntity, GraphPersonEntity destination) throws NullArgumentException;

    boolean deleteFollowingEdge(GraphPersonEntity source, GraphPersonEntity destination);

    /**
     * @param snUser is an incomplete object.
     * @return a list of ids corresponding to the users followed by @snUser.
     */
    List<String> getFollowedUsersIds(GraphPersonEntity snUser);

    /**
     * @param snUser is an incomplete object.
     * @return a list of ids corresponding to the users who are following @snUser.
     */
    List<String> getFollowersIds(GraphPersonEntity snUser);

    /**
     * Delete the vertex represented by a GraphPersonalityEntity and its vertices (in & out).
     *
     * @param snUser is a partial object.
     * @return true if the vertex was successfully deleted.
     */
    boolean deletePersonEntityFromGraph(GraphPersonEntity snUser);

    /**
     * @param snUser
     * @return the number of persons who are followed by @snUSer.
     */
    long countNumberOfFollowedPersons(GraphPersonEntity snUser);

    /**
     * @param snUser
     * @return the number of person who are following @snUser.
     */
    long countNumberOfFollowers(GraphPersonEntity snUser);
}
