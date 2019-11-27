#!/usr/bin/env bash
echo "Staring user service"
echo "#########################"
echo "Waiting for Eureka server to start"
echo "#########################"
while ! `nc -z eureka-server  8761`; do sleep 3; done
rm -rf /data/db
mkdir -p /data/db
mongod &
cd /usr/local/user-service/
java -jar user-service-0.0.1-SNAPSHOT.jar