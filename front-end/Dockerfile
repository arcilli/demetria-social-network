FROM openjdk:8-jre

RUN apt-get update && apt-get -y install netcat && apt-get clean
RUN mkdir /usr/local/frontend
COPY target/front-end-0.0.1-SNAPSHOT.jar /usr/local/frontend
COPY Docker/frontend.sh /
RUN chmod +x frontend.sh
CMD ./frontend.sh