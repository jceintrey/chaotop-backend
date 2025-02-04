# Maven image used to build
FROM maven:latest AS build

# Workdir
WORKDIR /app

# Copy pom.xml and run mvn dependency
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy source files
COPY src ./src

# Build the app with DskipTests to ovoid jdbc connection errors
RUN mvn clean install -DskipTests

# Build the final image with openjdk image
FROM openjdk:17-jdk-alpine

# workdir
WORKDIR /app

# Copy the previous generated jar file
COPY --from=build /app/target/chaotop-backend-0.0.1-SNAPSHOT.jar chaotop-backend.jar

# Entrypoint
ENTRYPOINT ["java", "-jar", "/app/chaotop-backend.jar"]
