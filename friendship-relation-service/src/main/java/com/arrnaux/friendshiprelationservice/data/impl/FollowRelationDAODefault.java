package com.arrnaux.friendshiprelationservice.data.impl;

import com.arrnaux.demetria.core.models.followRelation.GraphPersonEntity;
import com.arrnaux.friendshiprelationservice.config.ProdOrientConfig;
import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.dbConnection.DatabaseConnector;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import lombok.extern.java.Log;
import org.apache.commons.lang.NullArgumentException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log
public class FollowRelationDAODefault implements FollowRelationDAO {
    private final DatabaseConnector databaseConnector;

    public FollowRelationDAODefault(ProdOrientConfig prodOrientConfig) {
        log.info("FollowRelationDAO constructor");
        log.info("FollowRelationDAO DB config:" + prodOrientConfig);
        this.databaseConnector = new DatabaseConnector(prodOrientConfig);
    }

    private ODatabaseSession getSession() {
        ODatabaseSession session = databaseConnector.getSession();
        session.activateOnCurrentThread();
        return session;
    }

    @Override
    public OVertex storePerson(GraphPersonEntity graphPersonEntity) {
        OVertex vertexPerson = findByUserName(graphPersonEntity);
        if (null == vertexPerson) {
            // The person has no associated vertex in the graph.
            ODatabaseSession session = getSession();

            vertexPerson = session.newVertex("Person");
            vertexPerson.setProperty("userName", graphPersonEntity.getUserName());
            vertexPerson.setProperty("storedId", graphPersonEntity.getStoredId());
            session.save(vertexPerson);

            session.close();
        }
        return vertexPerson;
    }

    @Override
    public OVertex findByUserName(GraphPersonEntity graphPersonEntity) {
        if (null == graphPersonEntity || null == graphPersonEntity.getUserName()) {
            return null;
        }
        String statement = "SELECT FROM Person WHERE userName = ?";
        ODatabaseSession session = getSession();

        OResultSet rs = session.query(statement, graphPersonEntity.getUserName());
        int count = 0;
        Optional<OVertex> vertexPerson = Optional.empty();
        while (rs.hasNext()) {
            vertexPerson = rs.next().getVertex();
            ++count;
        }
        session.close();
        // Only one person is expected as result.
        if (1 == count && vertexPerson.isPresent()) {
            return vertexPerson.get();
        }
        return null;
    }

    @Override
    public OEdge storeFollowsEdge(OVertex source, OVertex destination) {
        try {
            OEdge edge = findFollowingEdge(
                    GraphPersonEntity.builder().userName(source.getProperty("userName")).build(),
                    GraphPersonEntity.builder().userName(destination.getProperty("userName")).build()
            );
            ODatabaseSession session = getSession();
            if (null == edge) {
                edge = source.addEdge(destination, "follows");
            }
            session.save(edge);
            return edge;
        } catch (NullArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    @Override
    public OEdge findFollowingEdge(GraphPersonEntity source, GraphPersonEntity destination)
            throws NullArgumentException {
        if (null == source || null == destination) {
            log.severe("Source or destination null when creating following relation edge");
            throw new NullArgumentException("Source or destination null.");
        }
        String statement;
        ODatabaseSession session = getSession();
        // For an edge, the input is where the arrow is (destinationPerson) and the out is the source of the edge.
        statement = "SELECT * FROM follows WHERE (in.userName= ? and out.userName= ?)";
        OResultSet rs = session.query(statement, destination.getUserName(), source.getUserName());
        // Only 1 result is expected.
        if (rs.hasNext()) {
            Optional<OEdge> edge = rs.next().getEdge();
            return edge.orElse(null);
        }
        session.close();
        return null;
    }

    @Override
    public boolean deleteFollowingEdge(GraphPersonEntity source, GraphPersonEntity destination) {
        try {
            OEdge edge = findFollowingEdge(source, destination);
            ODatabaseSession session = getSession();
            session.delete(edge);
            return true;
        } catch (NullArgumentException e) {
            log.severe("Source or destination null when deleting following edge");
        }
        return false;
    }

    @Override
    public List<String> getFollowedUsersIds(GraphPersonEntity snUser) {
        ODatabaseSession session = getSession();
        String query;
        OResultSet rs = null;
        // TODO: add a condition for checking that the relation is valid.
        if (null != snUser.getUserName()) {
            query = "SELECT in.storedId FROM follows where out.userName = ?";
            rs = session.query(query, snUser.getUserName());
        } else if (null != snUser.getStoredId()) {
            query = "SELECT in.storedId FROM follows where out.storedId= ?";
            rs = session.query(query, snUser.getStoredId());
        }
        assert null != rs;
        // TODO: replace this with lambda expressions.
        List<String> followedPersons = new ArrayList<>();
        while (rs.hasNext()) {
            OResult id = rs.next();
            followedPersons.add(id.getProperty("in.storedId"));
        }
        session.close();
        return followedPersons;
    }

    @Override
    public List<String> getFollowersIds(GraphPersonEntity snUser) {
        ODatabaseSession session = getSession();
        OResultSet rs = null;
        StringBuilder query = new StringBuilder("SELECT storedId FROM (SELECT EXPAND(IN()) FROM person WHERE ");
        if (null != snUser) {
            if (null != snUser.getStoredId()) {
                query.append("storedId = ?)");
                rs = session.query(query.toString(), snUser.getStoredId());
            } else if (null != snUser.getUserName()) {
                query.append("userName = ?)");
                rs = session.query(query.toString(), snUser.getUserName());
            }
            if (null != rs) {
                return rs.stream()
                        .map(k -> (String) k.getProperty("storedId"))
                        .collect(Collectors.toList());
            }
        }
        session.close();
        // Return an empty list.
        return new ArrayList<>();
    }

    @Override
    public boolean deletePersonEntityFromGraph(GraphPersonEntity snUser) {
        if (null == snUser || null == snUser.getStoredId()) {
            return false;
        }
        ODatabaseSession session = getSession();
        String query;
        OResultSet rs = null;
        if (null != snUser.getStoredId()) {
            query = "DELETE VERTEX Person where storedId= ?";
            rs = session.command(query, snUser.getStoredId());
        }
        int count = 0;
        while (null != rs && rs.hasNext()) {
            ++count;
            rs.next();
        }
        session.close();
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
        ODatabaseSession session = getSession();
        OResultSet rs = session.query(query, snUser.getStoredId());

        long val = -1;
        long count = 0;
        while (rs.hasNext()) {
            val = rs.next().getProperty(alias);
            ++count;
        }
        session.close();
        if (1 == count) {
            return val;
        }
        return (long) -1.0;
    }
}
