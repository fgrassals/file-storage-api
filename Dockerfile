FROM openjdk:11-jdk-slim

# Custom user so we don't run with root
RUN useradd files
USER files:files

# Copy the built jar from the gradle build directory into the container
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]