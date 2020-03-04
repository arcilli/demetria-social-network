package com.arrnaux.friendshiprelationservice.data.impl;

import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.dbConnection.Connection;
import com.arrnaux.friendshiprelationservice.model.FollowRelationValidity;
import com.arrnaux.friendshiprelationservice.model.Person;
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
    public OVertex storePerson(Person person) {
        OVertex vertexPerson = findByUserName(person);
        if (null == vertexPerson) {
            // The person has no vertex in the graph.
            vertexPerson = getConnection().getSession().newVertex("Person");
            vertexPerson.setProperty("userName", person.getUserName());
            vertexPerson.save();
        }
        return vertexPerson;
    }

    @Override
    public OVertex findByUserName(Person person) throws NullArgumentException {
        if (null == person || null == person.getUserName()) {
            throw new NullArgumentException("Person object is null.");
        }
        String statement = "SELECT FROM Person WHERE userName = ?";
        OResultSet rs = getConnection().getSession().query(statement, person.getUserName());
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
    public OEdge storeFollowingRelation(OVertex source, OVertex destination) {
        OEdge edge = findFollowingEdge(
                new Person(source.getProperty("userName")),
                new Person(destination.getProperty("userName")));
        if (null == edge) {
            edge = source.addEdge(destination, "follows");
            edge.setProperty("valid", true);
            edge.save();
        }
        return edge;
    }

    @Nullable
    @Override
    public OEdge findFollowingEdge(Person source, Person destination) throws NullArgumentException {
        if (null == source || null == destination) {
            throw new NullArgumentException("Source or destination null.");
        }
        String statement = "SELECT * FROM follows WHERE (in.userName= ? and out.userName= ?)";
        // For an edge, the input is where the arrow is (destinationPerson) and the out is the source of the edge.
        OResultSet rs = getConnection().getSession().query(statement, destination.getUserName(), source.getUserName());
        // Only 1 result is expected.
        if (rs.hasNext()) {
            Optional<OEdge> edge = rs.next().getEdge();
            return edge.orElse(null);
        }
        return null;
    }
}
