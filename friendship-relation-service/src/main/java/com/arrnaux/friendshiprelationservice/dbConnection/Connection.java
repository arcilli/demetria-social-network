package com.arrnaux.friendshiprelationservice.dbConnection;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Component
@Log4j
@Configuration
@EnableConfigurationProperties
public class Connection {

    @Value("${orientdb.connection.service.name}")
    private String serviceName;

    OrientDB database = null;
    ODatabaseSession session = null;

    public void openDefaultConnection() {
        loadDBDefaultConfig();
        newSessionDefaultConfig();
    }

    public void close() {
        session.close();
        session = null;
        database.close();
        database = null;
    }

    private void loadDBDefaultConfig() {
        if (null != serviceName) {
            log.info("Environment property: " + serviceName);
            String connectionURL = "remote:" + serviceName;
            database = new OrientDB(connectionURL, OrientDBConfig.defaultConfig());
        } else {
            log.info("Checking for localhost database.");
            database = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        }
        // Creates the database.
        try {
            database.open("test", "admin", "admin");
        } catch (Exception e) {
            database.create("test", ODatabaseType.PLOCAL);
            database.close();
        }
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
        OClass followRelation = session.createEdgeClass("follows");
        followRelation.createProperty("valid", OType.BOOLEAN);
    }
}
