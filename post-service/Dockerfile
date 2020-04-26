FROM openjdk:8-jre
ARG JAR_FILE
COPY ./target/${JAR_FILE} /usr/local/post-service/post-service.jar
WORKDIR /usr/local/post-service/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "post-service.jar"]