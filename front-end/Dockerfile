FROM openjdk:8-jre
RUN mkdir /usr/local/frontend
ARG JAR_FILE
COPY ./target/${JAR_FILE} /usr/local/frontend/frontend.jar
WORKDIR /usr/local/frontend/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "frontend.jar"]
