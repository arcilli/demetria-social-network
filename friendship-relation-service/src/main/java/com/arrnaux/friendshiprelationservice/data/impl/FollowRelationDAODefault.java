package com.arrnaux.friendshiprelationservice.data.impl;

import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.dbConnection.Connection;
import com.arrnaux.friendshiprelationservice.model.Person;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.springframework.stereotype.Service;

@Service
public class FollowRelationDAODefault implements FollowRelationDAO {

    // TODO: use singleton / autowired here.
    Connection connection = new Connection();

    @Override
    public OVertex storePerson(Person person) {
        // TODO: do this only if the person is not present
        // Otherwise, return the OVertex representing the graphPerson
        connection.openDefaultConnection();
        OVertex vertexPerson;

        vertexPerson = connection.getSession().newVertex("Person");
        // Check if the person already exists
        // If not, create it
        // Else, retrieve & return it.

        vertexPerson.setProperty("userName", person.getUserName());
        vertexPerson.save();
        connection.close();
        return vertexPerson;
    }

    @Override
    public OEdge storeFollowingRelation(OVertex source, OVertex destination) {
        connection.openDefaultConnection();
        // TODO: Check if the edge does not already exist.
        OEdge edge = source.addEdge(destination, "follows");
        edge.save();
        connection.close();
        return edge;
    }

    @Override
    public boolean existsFollowingRelationBetweenPeople(Person source, Person destination) {
        String statement = "SELECT COUNT(*) AS no_edges FROM follows WHERE (in.username= ? and out.username= ?)";
        OResultSet rs = connection.getSession().query(statement, source.getUserName(), destination.getUserName());
        while (rs.hasNext()) {
            if (1 == (int) (rs.next()).getProperty("no_edges")) {
                return true;
            }
        }
        return false;
    }
}
