# Use an official JDK 21 base image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy your built JAR into the container
COPY target/ewallet-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR when the container starts
# Expose the application port (mapped by docker run / docker-compose)
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
