#!/usr/bin/env bash
echo "Staring frontend"
echo "#########################"
echo "Waiting for Eureka server to start"
echo "#########################"
while ! `nc -z eureka-server  8761`; do sleep 3; done
echo "Waiting for User service to start"
echo "#########################"
while ! `nc -z user-service  8080`; do sleep 3; done
cd /usr/local/frontend
java -jar front-end-0.0.1-SNAPSHOT.jar