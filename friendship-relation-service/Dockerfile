FROM openjdk:8-jre

RUN mkdir /usr/local/friendship-relation-service
COPY target/friendship-relation-service-2.0-SNAPSHOT.jar /usr/local/friendship-relation-service/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/usr/local/friendship-relation-service/friendship-relation-service-2.0-SNAPSHOT.jar"]
