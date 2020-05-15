package com.arrnaux.friendshiprelationservice.dbConnection;

import com.arrnaux.friendshiprelationservice.config.ProdOrientConfig;
import com.orientechnologies.common.concur.OOfflineNodeException;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.ODatabaseType;
import com.orientechnologies.orient.core.db.OrientDB;
import com.orientechnologies.orient.core.db.OrientDBConfig;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import lombok.extern.java.Log;

@Log
public class DatabaseConnector {
    private OrientDB orientDB;

    private final ProdOrientConfig prodOrientConfig;

    public DatabaseConnector(ProdOrientConfig prodOrientConfig) {
        log.info("Creating orient connector after config: " + prodOrientConfig);
        this.prodOrientConfig = prodOrientConfig;
        configureDatabase();
    }

    public ODatabaseSession getSession() {
        ODatabaseSession session = orientDB.open("test", "admin", "admin");
        session.activateOnCurrentThread();
        return session;
    }

    private void configureDatabase() {
        if (null != prodOrientConfig && null != prodOrientConfig.getConnectionServiceName()) {
            String connectionURL = "remote:" + prodOrientConfig.getConnectionServiceName();
            String serverPassword = prodOrientConfig.getRootPassword();
            orientDB = new OrientDB(connectionURL, "root", serverPassword, OrientDBConfig.defaultConfig());
        } else {
            // Fallback on localhost
            log.info("Using orientDb locally.");
            orientDB = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
        }
        try {
            log.info("Creating OrientDB database");
            orientDB.createIfNotExists("test", ODatabaseType.PLOCAL, OrientDBConfig.defaultConfig());
            setupVerticesAndEdgeClasses();
        } catch (OOfflineNodeException e) {
            log.info("Offline node exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupVerticesAndEdgeClasses() {
        ODatabaseSession session = this.getSession();
        log.info("Setup vertices & edge classes for OrientDB");
        OClass person = session.createVertexClass("Person");
        if (null == person.getProperty("userName")) {
            person.createProperty("userName", OType.STRING);
        }
        // TODO: decide if using index
//        if (!person.areIndexed(OClass.VERTEX_CLASS_NAME)) {
//            person.createIndex("PersonUserNameIndex", OClass.INDEX_TYPE.UNIQUE, OClass.VERTEX_CLASS_NAME);
//        }
        OClass followRelation = session.createEdgeClass("follows");
        session.close();
    }
}
