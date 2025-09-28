# Use official Java 17 image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for caching dependencies)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give execute permission to Maven wrapper
RUN chmod +x mvnw

# Copy source code
COPY src ./src

# Build the Spring Boot jar (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Copy the built jar
COPY target/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
