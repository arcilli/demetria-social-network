FROM openjdk:8-jre
RUN mkdir /usr/local/post-service
COPY target/post-service-1.0-SNAPSHOT.jar /usr/local/post-service
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/usr/local/post-service/post-service-1.0-SNAPSHOT.jar"]