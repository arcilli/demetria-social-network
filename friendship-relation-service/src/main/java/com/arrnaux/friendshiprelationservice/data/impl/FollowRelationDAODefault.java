package com.arrnaux.friendshiprelationservice.data.impl;

import com.arrnaux.demetria.core.followRelation.model.FollowRelationValidity;
import com.arrnaux.demetria.core.followRelation.model.GraphPersonEntity;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.dbConnection.Connection;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

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
                GraphPersonEntity.builder().userName(source.getProperty("userName")).build()
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

}
