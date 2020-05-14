#!/bin/bash
# TODO: replace it with a for
while ! $(nc -z orient-db 2424); do
  sleep 3
done
# If the database is already present, it's assumed that it contains already dump data.
/orientdb/bin/console.sh "CREATE DATABASE remote:orient-db/test root rootpwd; IMPORT DATABASE initial-db.gz -preserveClusterIDs=true"
