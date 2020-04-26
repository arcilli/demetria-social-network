FROM openjdk:8-jre

RUN mkdir /usr/local/eureka-server
ARG JAR_FILE
ADD ./target/${JAR_FILE} /usr/local/eureka-server/eureka-service.jar
WORKDIR /usr/local/eureka-server/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "eureka-service.jar"]