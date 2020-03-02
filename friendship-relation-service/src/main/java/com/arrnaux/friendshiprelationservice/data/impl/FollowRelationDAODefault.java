package com.arrnaux.friendshiprelationservice.data.impl;

import com.arrnaux.friendshiprelationservice.data.FollowRelationDAO;
import com.arrnaux.friendshiprelationservice.dbConnection.Connection;
import com.arrnaux.friendshiprelationservice.model.Person;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import org.springframework.stereotype.Service;

@Service
public class FollowRelationDAODefault implements FollowRelationDAO {

    Connection connection = new Connection();

    @Override
    public OVertex storePerson(Person person) {
        // TODO: do this only if the person is not present
        // Otherwise, return the OVertex representing the graphPerson
        connection.openDefaultConnection();
        OVertex vertexPerson;

        vertexPerson= connection.getSession().newVertex("Person");
        // Check if the person already exists
        // If not, create it
        // Else, retrieve & return it.

        vertexPerson.setProperty("username", person.getUserName());
        vertexPerson.save();
        connection.close();
        return vertexPerson;
    }

    @Override
    public void storeFollowingRelation(OVertex source, OVertex destination) {
        connection.openDefaultConnection();
        OEdge edge = source.addEdge(destination, "follows");
        edge.save();
        connection.close();
    }
}
