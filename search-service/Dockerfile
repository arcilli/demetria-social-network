FROM openjdk:8-jre

RUN mkdir /usr/local/search-service
COPY target/search-service-1.0-SNAPSHOT.jar /usr/local/search-service/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/usr/local/search-service/search-service-1.0-SNAPSHOT.jar"]