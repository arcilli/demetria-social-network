#!/usr/bin/env bash
echo "Staring user service"
echo "#########################"
java -jar -Dspring.profiles.active=prod /usr/local/user-service/user-service-1.0-SNAPSHOT.jar
