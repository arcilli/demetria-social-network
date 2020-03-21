FROM openjdk:8-jre

RUN mkdir /usr/local/frontend
COPY target/front-end-1.0-SNAPSHOT.jar /usr/local/frontend
COPY Docker/frontend.sh /
RUN chmod +x frontend.sh
CMD ./frontend.sh