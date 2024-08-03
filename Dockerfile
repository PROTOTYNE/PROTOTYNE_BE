# Use the official OpenJDK image as the base image
FROM openjdk:17-jdk-alpine

ARG PROFILES
ARG ENV

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build directory to the working directory
COPY build/libs/*.jar app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${PROFILES} -jar app.jar"]
