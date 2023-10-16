# Use an official OpenJDK runtime as a parent image
FROM openjdk:17

# Set the working directory in the container
WORKDIR /app

# Copy the application JAR file into the container
COPY target/SistemaInventario-0.0.1-SNAPSHOT.jar app.jar

# Run the application when the container starts
CMD ["java", "-jar", "app.jar"]