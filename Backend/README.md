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

This hybrid approach optimizes for both structured transactions and flexible product schemas.

---

## 🛠️ Technologies

- **Framework**: Spring Boot 3.5.x
- **Language**: Java 24
- **Security**: Spring Security 6, JWT (JJWT)
- **Data Access**: Spring Data JPA (Hibernate), Spring Data MongoDB
- **API Documentation**: SpringDoc OpenAPI (Swagger UI)
- **Build Tool**: Maven (wrapper included)
- **Database Drivers**: MySQL Connector/J, MongoDB Java Driver

---

## 📦 Prerequisites

- Java 24 (or 21+)
- Maven (or use the wrapper)
- MySQL 8.0+ & MongoDB 6.0+ (local or cloud)

---

## ⚙️ Configuration

All configuration is in `src/main/resources/application.properties` (or `application-{profile}.properties`). Key settings:

```properties
# Server
server.port=${PORT:8080}

# MySQL
spring.datasource.url=${MYSQL_URL:jdbc:mysql://localhost:3306/maison_noir}
spring.datasource.username=${MYSQL_USER:root}
spring.datasource.password=${MYSQL_PASSWORD:}

# MongoDB
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/maison_noir}

# JWT
jwt.secret=${JWT_SECRET:your_jwt_secret_key}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

Override any setting using environment variables – this is especially useful for deployment.

---

## 🚀 Running the Backend

### Using Maven Wrapper

```bash
# Linux/macOS
./mvnw clean spring-boot:run

# Windows
.\mvnw clean spring-boot:run 
```

### Build a JAR and Run

```bash
./mvnw clean package
java -jar target/Backend-*.jar
```

---

## 🧪 API Documentation

Once running, visit Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

You can also download the OpenAPI spec at:

```text
http://localhost:8080/v3/api-docs
```

---

## 🔐 Security & Authentication

The API uses **JWT tokens** for stateless authentication. To access protected endpoints, include the token in the `Authorization` header:

```text
Authorization: Bearer <jwt_token>
```

Public endpoints (e.g., login, register, product listing) are exempt.

**Roles:**

- **ROLE_ADMIN** – Full access to admin endpoints

- **ROLE_CUSTOMER** – Standard user permissions

---

## 🗄️ Database Schema

- MySQL schema is defined via Hibernate DDL (`ddl-auto=update`) – see `src/main/resources/db/mysql/schema.sql` for reference.

- MongoDB collections are validated by `src/main/resources/db/mongodb/schema.js`.

See the [Database README](src/main/resources/db/README.md) for seeding instructions.

---

## 🧹 Testing

Run unit and integration tests:

```bash
./mvnw test
```

For test coverage, use:

```bash

```

---

## 📦 Deployment

The backend is deployed on **Render** as a Docker container. The `Dockerfile` at the project root builds both frontend and backend into a single image. Ensure environment variables (database URLs, JWT secret) are set in Render’s dashboard.

---

## 🤝 Contributing

Follow the same guidelines as the main project. Please write unit tests for new features.

---

## 📄 License

Proprietary – all rights reserved.
