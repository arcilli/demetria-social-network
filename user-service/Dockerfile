
FROM openjdk:8-jre

RUN mkdir /usr/local/user-service
COPY target/user-service-1.0-SNAPSHOT.jar /usr/local/user-service/
COPY Docker/user-service.sh /
RUN chmod +x user-service.sh
CMD ./user-service.sh