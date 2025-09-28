FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Explicitly copy from repo root
COPY ./app.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
