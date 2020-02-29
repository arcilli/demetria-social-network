package com.arrnaux.friendshiprelationservice;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.record.OVertex;

public class Person {

    public static OVertex createPerson(ODatabaseSession db, String name){
        OVertex result = db.newVertex("Person");
        result.setProperty("username", name);
        result.save();
        return result;
    }
}
