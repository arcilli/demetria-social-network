#!/usr/bin/env bash

cd ../eureka-server
mvn package
cd ../user-service
mvn package
cd ../
docker-compose up