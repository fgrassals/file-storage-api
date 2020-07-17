# stage used to build the project
FROM gradle:6.5.1-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# if tests are run, the test that checks if the spring context runs fails
# because it depends on the database container being started, and this isn't
# the case when running the build
RUN gradle build --no-daemon -x test

# stage used to run the project
FROM openjdk:11-jdk-slim

# installs wait-for-it so the spring boot app starts ONLY after the db has started
RUN apt update && apt install -y wait-for-it

# Copy the built jar from the gradle build directory into the container
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

ENTRYPOINT ["wait-for-it", "db:3306", "--", "java", "-jar", "/app/app.jar"]
