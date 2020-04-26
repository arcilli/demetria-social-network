FROM openjdk:8-jre

RUN mkdir /usr/local/friendship-relation-service
ARG JAR_FILE
WORKDIR /usr/local/friendship-relation-service
COPY ./target/${JAR_FILE}  /usr/local/friendship-relation-service/friendship-relation-service.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "friendship-relation-service.jar"]