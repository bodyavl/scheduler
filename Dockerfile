# Use an official Gradle image to build the application
FROM gradle:jdk21 AS build

# Set the working directory
WORKDIR /home/gradle/project

# Copy only necessary files for dependency download
COPY . .

# Download dependencies
RUN ./gradlew dependencies

# Copy the remaining project files
COPY . .

# Build the project
RUN ./gradlew build

# Use a minimal base image for the runtime
FROM eclipse-temurin:21-jre

# Set the working directory
WORKDIR /app

# Copy the built application from the build stage
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

# Expose the port the application will run on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
