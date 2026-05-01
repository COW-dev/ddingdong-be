# Build stage
FROM gradle:jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN ./gradlew clean build -x test --no-daemon

# Package stage
FROM eclipse-temurin:21-jdk
RUN apt-get update \
    && apt-get install -y --no-install-recommends fontconfig fonts-noto-cjk \
    && rm -rf /var/lib/apt/lists/*
COPY --from=build /home/gradle/src/build/libs/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
