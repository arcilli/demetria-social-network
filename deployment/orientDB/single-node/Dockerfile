#OrientDB orientDB = new OrientDB("remote:localhost", OrientDBConfig.defaultConfig());
#OrientDBConfig config = OrientDBConfig.builder().addConfig(CLIENT_CONNECTION_STRATEGY, "ROUND_ROBIN_CONNECT").build();
#final ODatabaseDocument db = orientDB.open("demo", user, password, config);

#remote:server1:2424;server2:8888;server3/mydb

# Dockerfile for running orientDB in distributed mode, with configured replica.
FROM orientdb:3.0.30
# run a script that creates the files for config & initialdatabase
ENTRYPOINT ["/orientdb/bin/server.sh", "-Ddistributed=true"]
