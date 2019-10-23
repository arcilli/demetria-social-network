#TODO: use current version of jar, not a hard-coded one (maybe using environment variables)
#TODO: move running script to /usr/local/eureka/server/
FROM openjdk:8-jre

RUN apt-get update && apt-get -y install netcat && apt-get clean
RUN mkdir /usr/local/hello-world
COPY target/hello-world-0.0.1-SNAPSHOT.jar /usr/local/hello-world/
COPY Docker/runHelloWorld.sh /
RUN chmod +x runHelloWorld.sh
CMD ./runHelloWorld.sh