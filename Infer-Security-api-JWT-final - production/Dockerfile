# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container at /app
COPY target/securitylayer-0.0.1-SNAPSHOT.jar /app/SecurityLayer.jar

# Expose the port the app runs on
EXPOSE 8080

# Define the command to run your application
ENTRYPOINT ["java", "-jar", "SecurityLayer.jar"]
