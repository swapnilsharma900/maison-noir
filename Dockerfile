# Build stage - Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy backend files
COPY Backend/pom.xml ./pom.xml
RUN mvn dependency:go-offline

# Copy source code
COPY Backend/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the JAR
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]