package com.arrnaux.friendshiprelationservice.data.impl;

import com.arrnaux.demetria.core.models.followRelation.FollowRelationValidity;
import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.dbConnection.Connection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FollowRelationDAODefault implements FollowRelationDAO {

    // TODO: implement this as a singleton or something else. Now, the connection is neved closed.
    public Connection getConnection() {
        Connection connection = new Connection();
        connection.openDefaultConnection();
        connection.getSession().activateOnCurrentThread();
        return connection;
    }

    @Override
    public OVertex storePerson(GraphPersonEntity graphPersonEntity) {
        OVertex vertexPerson = findByUserName(graphPersonEntity);
        if (null == vertexPerson) {
            // The person has no associated vertex in the graph.
            vertexPerson = getConnection().getSession().newVertex("Person");
            // TODO: set a property for userID retrieved from user service.
            vertexPerson.setProperty("userName", graphPersonEntity.getUserName());
            vertexPerson.setProperty("storedId", graphPersonEntity.getStoredId());
            vertexPerson.save();
        }
        return vertexPerson;
    }

    @Override
    public OVertex findByUserName(GraphPersonEntity graphPersonEntity) throws NullArgumentException {
        if (null == graphPersonEntity || null == graphPersonEntity.getUserName()) {
            throw new NullArgumentException("Person object is null.");
        }
        String statement = "SELECT FROM Person WHERE userName = ?";
        OResultSet rs = getConnection().getSession().query(statement, graphPersonEntity.getUserName());
        int count = 0;
        Optional<OVertex> vertexPerson = Optional.empty();
        while (rs.hasNext()) {
            vertexPerson = rs.next().getVertex();
            ++count;
        }
        // Only one person is expected as result.
        if (1 == count && vertexPerson.isPresent()) {
            return vertexPerson.get();
        }
        return null;
    }

    @Override
    public OEdge storeValidFollowingRelation(OVertex source, OVertex destination) {
        OEdge edge = findFollowingEdge(
                GraphPersonEntity.builder().userName(source.getProperty("userName")).build(),
                GraphPersonEntity.builder().userName(destination.getProperty("userName")).build()
        );
        if (null == edge) {
            edge = source.addEdge(destination, "follows");
        }
        edge.setProperty("valid", true);
        edge.save();
        return edge;
    }

    @Nullable
    @Override
    public OEdge findFollowingEdge(GraphPersonEntity source, GraphPersonEntity destination, FollowRelationValidity... followRelationValidity)
            throws NullArgumentException {
        if (null == source || null == destination) {
            throw new NullArgumentException("Source or destination null.");
        }
        String statement;
        OResultSet rs;
        // For an edge, the input is where the arrow is (destinationPerson) and the out is the source of the edge.
        if (0 == followRelationValidity.length) {
            statement = "SELECT * FROM follows WHERE (in.userName= ? and out.userName= ?)";
            rs = getConnection().getSession().query(statement, destination.getUserName(), source.getUserName());
        } else {
            statement = "SELECT * FROM follows WHERE (in.userName= ? and out.userName= ? and valid= ?)";
            rs = getConnection().getSession().query(statement, destination.getUserName(), source.getUserName(),
                    followRelationValidity[0].getValue());
        }
        // Only 1 result is expected.
        if (rs.hasNext()) {
            Optional<OEdge> edge = rs.next().getEdge();
            return edge.orElse(null);
        }
        return null;
    }

    @Nullable
    @Override
    public OEdge invalidateFollowingEdge(GraphPersonEntity source, GraphPersonEntity destination) {
        OEdge edge = findFollowingEdge(source, destination);
        if (null != edge) {
            edge.setProperty("valid", false);
            edge.save();
            return edge;
        }
        return null;
    }

    @Nullable
    @Override
    public List<String> getFollowedUsersIds(GraphPersonEntity snUser) {
        String query;
        OResultSet rs = null;
        // TODO: add a condition for checking that the relation is valid.
        if (null != snUser.getUserName()) {
            query = "SELECT in.storedId FROM follows where out.userName = ?";
            rs = getConnection().getSession().query(query, snUser.getUserName());
        } else if (null != snUser.getStoredId()) {
            query = "SELECT in.storedId FROM follows where out.storedId= ?";
            rs = getConnection().getSession().query(query, snUser.getStoredId());
        }
        assert null != rs;
        // TODO: replace this with lambda expressions.
        List<String> followedPersons = new ArrayList<>();
        while (rs.hasNext()) {
            OResult id = rs.next();
            followedPersons.add(id.getProperty("in.storedId"));
        }
        return followedPersons;
    }

    @Override
    public boolean deletePersonEntityFromGraph(GraphPersonEntity snUser) {
        if (null == snUser || null == snUser.getStoredId()) {
            return false;
        }

        String query;
        OResultSet rs = null;
        if (null != snUser.getStoredId()) {
            query = "DELETE VERTEX Person where storedId= ?";
            rs = getConnection().getSession().command(query, snUser.getStoredId());
        }
        int count = 0;
        while (null != rs && rs.hasNext()) {
            ++count;
            rs.next();
        }
        return 1 == count;
    }

    @Override
    public long countNumberOfFollowedPersons(GraphPersonEntity snUser) {
        if (null == snUser || null == snUser.getStoredId()) {
            return -1;
        }

        String alias = "noFollowedPersons";
        String query = "SELECT COUNT(*) AS " + alias + " FROM follows WHERE out.storedId= ?";
        return processCountQuery(snUser, query, alias);
    }

    @Override
    public long countNumberOfFollowers(GraphPersonEntity snUser) {
        if (null == snUser || null == snUser.getStoredId()) {
            return -1;
        }

        String alias = "noFollowers";
        String query = "SELECT COUNT(*) AS " + alias + " FROM follows WHERE in.storedId = ?";
        return processCountQuery(snUser, query, alias);
    }

    private long processCountQuery(GraphPersonEntity snUser, String query, String alias) {
        OResultSet rs = getConnection().getSession().query(query, snUser.getStoredId());
        long val = -1;
        long count = 0;
        while (rs.hasNext()) {
            val = rs.next().getProperty(alias);
            ++count;
        }
        if (1 == count) {
            return val;
        }
        return (long)-1.0;
    }
}
