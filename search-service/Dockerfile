FROM openjdk:8-jre
ARG JAR_FILE
RUN mkdir /usr/local/search-service
COPY target/${JAR_FILE} /usr/local/search-service/
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/usr/local/search-service/${JAR_FILE}"]