#!/usr/bin/env bash
echo "Staring friendship-relation service"
echo "#########################"
cd /usr/local/friendship-relation-service/
java -jar -Dspring.profiles.active=prod friendship-relation-service-1.0-SNAPSHOT.jar
