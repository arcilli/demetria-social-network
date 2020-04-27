FROM openjdk:8-jre

ARG JAR_FILE
RUN mkdir /usr/local/user-service
COPY ./target/${JAR_FILE} /usr/local/user-service/user-service.jar
WORKDIR /usr/local/user-service/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "user-service.jar"]