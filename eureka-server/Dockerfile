#This should/not using an entrypoint?
#TODO: use current version of jar, not a hard-coded one (maybe using environment variables)
#TODO: move running script to /usr/local/eureka/server/
FROM openjdk:8-jre

RUN mkdir /usr/local/eureka-server
COPY target/eureka-server-0.0.1-SNAPSHOT.jar /usr/local/eureka-server/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/usr/local/eureka-server/eureka-server-0.0.1-SNAPSHOT.jar"]