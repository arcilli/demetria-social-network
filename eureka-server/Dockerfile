#FROM openjdk:8-jre
#
#ADD target/eureka-server-0.0.1-SNAPSHOT.jar /usr/local/eureka-server/eureka-server.jar
##WORKDIR /usr/local/eureka-server/
#

#This should/not using an entrypoint?
#TODO: use current version of jar, not a hard-coded one (maybe using environment variables)
#TODO: move running script to /usr/local/eureka/server/
FROM openjdk:8-jre

RUN mkdir /usr/local/eureka-server
COPY target/eureka-server-0.0.1-SNAPSHOT.jar /usr/local/eureka-server/
COPY Docker/run.sh /
RUN chmod +x run.sh
CMD ./run.sh