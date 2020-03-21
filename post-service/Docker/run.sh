#!/bin/bash
echo "Staring post service"
echo "#########################"
java -jar -Dspring.profiles.active=prod /usr/local/frontend/post-service-1.0-SNAPSHOT.jar
