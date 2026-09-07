# Stage 1: Build Frontend
FROM node:18-slim AS frontend-build

WORKDIR /app/Frontend
COPY Frontend/package*.json ./
RUN npm install
COPY Frontend/ ./
RUN npm run build

# ✅ Debug: Verify build output
RUN echo "=== Frontend build complete ===" && \
    ls -la /app/Frontend/ && \
    ls -la /app/Frontend/dist/ && \
    ls -la /app/Frontend/dist/assets/ | head -10

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

# ✅ DEBUG: Check if files exist
RUN echo "=== Checking if static files were copied ===" && \
    ls -la ./src/main/resources/static/ && \
    echo "=== Looking for index.html ===" && \
    find ./src/main/resources/static/ -name "*.html" && \
    echo "=== All files in static ===" && \
    ls -la ./src/main/resources/static/ \
    test -f ./src/main/resources/static/index.html && echo "✅ index.html found!" || echo "❌ index.html NOT found!"

# Build Backend
RUN mvn clean package -DskipTests

# ✅ DEBUG: Check JAR contents for index.html
RUN echo "=== Checking JAR contents ===" && \
    jar tf target/*.jar | grep -E "static/index\\.html|BOOT-INF/classes/static" || echo "❌ index.html NOT in JAR!"

# ✅ DEBUG: Check JAR contents
RUN echo "=== Checking JAR contents for static files ===" && \
    jar tf target/*.jar | grep -E "static|index\\.html" | head -20

# Run stage
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy the JAR
COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
