package com.arrnaux.friendshiprelationservice;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.record.OEdge;
import com.orientechnologies.orient.core.record.OVertex;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "test")
public class Test {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String MakeRequest() {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabaseSession db = orient.open("test", "admin", "admin");

        OClass person = db.getClass("Person");
        if (null == person) {
            person = db.createVertexClass("Person");
        }
        if (person.getProperty("username") == null) {
            person.createProperty("username", OType.STRING);
        }
        //                     index name                index type          property name
        person.createIndex("Person_username_index", OClass.INDEX_TYPE.UNIQUE, "username");

        if (null == db.getClass("FriendOf")) {
            db.createEdgeClass("FriendOf");
        }

        db.close();
        orient.close();
        return "Hello";
    }

    @RequestMapping(value = "createPeople", method = RequestMethod.GET)
    private static void createPeople() {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabaseSession db = orient.open("test", "admin", "admin");
        OVertex alice = Person.createPerson(db, "Alice1");
        OVertex bob = Person.createPerson(db, "Bob2");
        OVertex jim = Person.createPerson(db, "Jim3");

//        Alice --FriendOf-->Bob --FriendOf--> Jim

        OEdge edge1 = alice.addEdge(bob, "FriendOf");
        edge1.save();

        OEdge edge2 = bob.addEdge(jim, "FriendOf");
        edge2.save();
        db.close();
        orient.close();
    }

    @RequestMapping(value = "FoaF", method = RequestMethod.GET)
    private static void executeAQuery() {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabaseSession db = orient.open("test", "admin", "admin");

        String query = "SELECT expand(out('FriendOf').out('FriendOf')) from Person where username =?";
        OResultSet rs = db.query(query, "Alice1");

        rs.stream().forEach(x -> System.out.println("friend: " + x.getProperty("username")));
        rs.close();

        db.close();
        orient.close();
    }

    @RequestMapping(value = "friendsWithBoth", method = RequestMethod.GET)
    public static void executeFriendsWithBoth() {
        OrientDB orient = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        ODatabaseSession db = orient.open("test", "admin", "admin");
        String query =
                "MATCH" +
                        "{class:Person, as:a, where: (username = :username1)}," +
                        "{class:Person, as:b, where: (username = :username2)}," +
                        "{as: a} -FriendOf-> {as:x} -FriendOf-> {as:b}" +
                        "RETURN x.username as friend";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("username1", "Alice1");
        params.put("name2", "Jim");
        OResultSet rs = db.query(query, params);


        db.close();
        orient.close();
    }
}
