FROM openjdk:8-jre

RUN mkdir /usr/local/post-service
COPY target/post-service-1.0-SNAPSHOT.jar /usr/local/post-service
COPY Docker/run.sh /
RUN chmod +x run.sh
CMD ./run.sh