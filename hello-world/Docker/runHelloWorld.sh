#!/usr/bin/env bash
echo "Staring hello-world service"
echo "#########################"
echo "Waiting for Eureka server to start"
echo "#########################"
while ! `nc -z eureka-server  8761`; do sleep 3; done
cd /usr/local/hello-world/
java -jar hello-world-0.0.1-SNAPSHOT.jar