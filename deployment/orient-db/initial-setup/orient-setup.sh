#!/bin/bash
while ! $(nc -z orient-db 2424); do
  sleep 3
done
/orientdb/bin/console.sh "CREATE DATABASE remote:orient-db/test root rootpwd; IMPORT DATABASE initial-db.gz -preserveClusterIDs=true"