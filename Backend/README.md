# Maison Noir Backend

This directory contains the backend application for the Maison Noir e-commerce platform.

## Architecture & Technologies

- **Framework:** Spring Boot 3.5.x
- **Language:** Java 24
- **Databases:**
  - **MySQL:** Relational data (Users, Addresses, Carts, Orders)
  - **MongoDB:** Document data (Product Catalog & Variants)
- **Security:** Spring Security with JWT (JSON Web Token) Authentication
- **API Documentation:** SpringDoc OpenAPI (Swagger UI)
- **Data Access:** Spring Data JPA (Hibernate), Spring Data MongoDB
- **Build Tool:** Maven

## Setup & Running Locally

### 1. Database Initialization
Before starting the backend, ensure your MySQL and MongoDB instances are running and properly seeded.
Please refer to the [Database Documentation](src/main/resources/db/README.md) (`Backend/src/main/resources/db/README.md`) for detailed instructions on setting up and seeding the databases.

### 2. Configuration
Check `src/main/resources/application.properties` (or your active profile config) to ensure your database connection URLs, usernames, and passwords are correct. Also, ensure your JWT secret key is configured.

### 3. Build and Run
You can run the application from the root of the `Backend` directory using the included Maven wrapper:

```powershell
# Windows
.\mvnw spring-boot:run

# Linux/macOS
./mvnw spring-boot:run
```

Once the application is running, the REST API will be available. You can interact with the API and view the documentation via Swagger UI, typically accessible at:
`http://localhost:8080/swagger-ui.html`

## Default Credentials

For local development and testing, the seeded database includes the following default users:

| Role | Email | Password |
|------|-------|----------|
| **ADMIN** | `admin@maisonnoir.in` | `Admin@123` |
| CUSTOMER | `rahul.sharma@gmail.com` | `Customer@123` |
| CUSTOMER | `priya.patel@gmail.com` | `Customer@123` |
| CUSTOMER | `arjun.kumar@gmail.com` | `Customer@123` |

> **Note:** Passwords in the database are BCrypt-encoded. Use the plain-text passwords listed above for login through the API or Swagger UI.
