#TODO: use current version of jar, not a hard-coded one (maybe using environment variables)
#TODO: move running script to /usr/local/eureka/server/
FROM openjdk:8-jre

RUN apt-get update && apt-get -y install mongodb netcat && apt-get clean
RUN mkdir /usr/local/user-service
COPY target/user-service-0.0.1-SNAPSHOT.jar /usr/local/user-service/
COPY Docker/user-service.sh /
RUN chmod +x user-service.sh
CMD ./user-service.sh