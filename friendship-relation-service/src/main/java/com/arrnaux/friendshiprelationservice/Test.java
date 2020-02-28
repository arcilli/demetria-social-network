package com.arrnaux.friendshiprelationservice;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
