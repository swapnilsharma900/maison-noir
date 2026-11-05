# Maison Noir Backend

This directory contains the Spring Boot backend application for the Maison Noir e‑commerce platform.

---

## 🏗️ Architecture Overview
The backend is built using a layered architecture:
- **Controller** – REST endpoints (Spring MVC)
- **Service** – Business logic
- **Repository** – Data access (Spring Data JPA / MongoDB)
- **Security** – JWT authentication & role‑based authorization

### Database Strategy
- **MySQL** – Stores users, addresses, carts, orders (relational data)
- **MongoDB** – Stores product catalog and variants (document data)

---

## ⚙️ Configuration & Environment Profiles

The backend utilizes Spring Profiles to separate local development from production. 

### 1. Local Development (`application-dev.properties`)
Use this profile for running the application on your local machine. It connects to localhost databases and **automatically creates and seeds the database** to save setup time.

```properties
# Server
server.port=8080

# MySQL - Notice 'createDatabaseIfNotExist=true' which prevents "No database selected" errors
spring.datasource.url=jdbc:mysql://localhost:3306/maison_noir?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_local_password

# Auto-execute schema.sql and data.sql on startup
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/maison_noir

# JWT
jwt.secret=local_development_secret_key_change_me
jwt.expiration=86400000
```

### 2. Production (`application-prod.properties`)
Used when deploying to Render or other cloud providers. This relies on Environment Variables (`${...}`) injected by the host, and **disables** automatic schema initialization to protect production data.

```properties
# Server
server.port=${PORT:8080}

# MySQL (Aiven / Cloud)
spring.datasource.url=${MYSQL_URL}
spring.datasource.username=${MYSQL_USER}
spring.datasource.password=${MYSQL_PASSWORD}

# NEVER auto-init data in production
spring.sql.init.mode=never
spring.jpa.hibernate.ddl-auto=validate

# MongoDB (Atlas)
spring.data.mongodb.uri=${MONGODB_URI}

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

*To switch profiles locally, set `spring.profiles.active=dev` in your `application.properties`.*

---

## 🚀 Running the Backend

### Using Maven Wrapper
```bash
# Linux/macOS
./mvnw clean spring-boot:run -Dspring-boot.run.profiles=dev

# Windows
.\mvnw clean spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 🔐 Security & Authentication
The API uses **JWT tokens** for stateless authentication. To access protected endpoints, include the token in the `Authorization` header:
`Authorization: Bearer <jwt_token>`

Roles:
- **ROLE_ADMIN** – Full access to admin endpoints
- **ROLE_CUSTOMER** – Standard user permissions
