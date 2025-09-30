# Build stage
FROM gradle:jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew clean build -x test --no-daemon

# Package stage
FROM openjdk:17
COPY otel/opentelemetry-javaagent.jar /otel/opentelemetry-javaagent.jar
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", \
    "-javaagent:/otel/opentelemetry-javaagent.jar", \
    "-jar", "/app.jar"]
