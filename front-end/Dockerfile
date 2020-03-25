FROM openjdk:8-jre
RUN mkdir /usr/local/frontend
COPY target/front-end-2.0-SNAPSHOT.jar /usr/local/frontend
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/usr/local/frontend/front-end-2.0-SNAPSHOT.jar"]