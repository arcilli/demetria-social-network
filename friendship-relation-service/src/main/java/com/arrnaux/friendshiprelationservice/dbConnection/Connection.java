package com.arrnaux.friendshiprelationservice.dbConnection;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Component
public class Connection {
    OrientDB database;
    ODatabaseSession session;

    public void openDefaultConnection() {
        loadDBDefaultConfig();
        newSessionDefaultConfig();
    }

    public void close() {
        session.close();
        database.close();
    }

    private void loadDBDefaultConfig() {
        database = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
    }

    private void newSessionDefaultConfig() {
        session = database.open("test", "admin", "admin");
        if (null == session.getClass("Person") || null == session.getClass("follows")) {
            setupGraphTypes();
        }
    }

    private void setupGraphTypes() {
        OClass person = session.createVertexClass("Person");
        if (null == person.getProperty("userName")) {
            person.createProperty("userName", OType.STRING);
        }
        // TODO: decide if using index
//        if (!person.areIndexed(OClass.VERTEX_CLASS_NAME)) {
//            person.createIndex("PersonUserNameIndex", OClass.INDEX_TYPE.UNIQUE, OClass.VERTEX_CLASS_NAME);
//        }
        session.createEdgeClass("follows");
    }
}
