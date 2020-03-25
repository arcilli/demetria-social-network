FROM openjdk:8-jre

RUN mkdir /usr/local/user-service
COPY target/user-service-2.0-SNAPSHOT.jar /usr/local/user-service/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/usr/local/user-service/user-service-2.0-SNAPSHOT.jar"]