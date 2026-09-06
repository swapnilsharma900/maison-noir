# Stage 1: Build Frontend
FROM node:18-slim AS frontend-build

WORKDIR /app/Frontend
COPY Frontend/package*.json ./
RUN npm install
COPY Frontend/ ./
RUN npm run build

# Stage 2: Build Backend (with frontend files already in place)
# Build stage - Maven
FROM maven:3.9.6-eclipse-temurin-21 AS backend-build
WORKDIR /app

# Copy backend files
COPY Backend/pom.xml ./pom.xml
RUN mvn dependency:go-offline

# Copy source code
COPY Backend/src ./src

# ✅ Copy frontend build directly to src/main/resources/static
# This way Maven doesn't need to copy anything
COPY --from=frontend-build /app/Frontend/dist ./src/main/resources/static

# Build Backend
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the JAR
COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
