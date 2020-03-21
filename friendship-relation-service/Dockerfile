
FROM openjdk:8-jre

RUN mkdir /usr/local/friendship-relation-service
COPY target/friendship-relation-service-1.0-SNAPSHOT.jar /usr/local/friendship-relation-service/
COPY Docker/run.sh /
RUN chmod +x run.sh
CMD ./run.sh