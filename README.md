# Maison Noir

### *The House of Vigour*

Maison Noir is a full‑stack e‑commerce platform for luxury men’s fashion. It combines a modern React frontend with a robust Spring Boot backend, using both MySQL and MongoDB to handle relational and document data efficiently.

---

## ✨ Features
- 🛍️ Product catalog with variants (MongoDB)
- 🛒 Shopping cart & order management (MySQL)
- 🔐 JWT‑based authentication & role‑based access (Admin / Customer)
- 📦 Dual‑database strategy for optimal performance
- 📱 Fully responsive UI with Tailwind CSS & Material UI
- 🧪 Swagger API documentation
- 🚀 Deployed on Render

---

## 🧱 Tech Stack
| Layer         | Technologies                                            |
| ------------- | ------------------------------------------------------- |
| **Frontend**  | React 19, Vite 7, Tailwind CSS 4, Material UI (MUI)     |
| **Backend**   | Spring Boot 3.5.x, Java 24, Spring Security, JWT, Maven |
| **Databases** | MySQL (relational), MongoDB (document)                  |
| **DevOps**    | Docker, Render                                          |

---

## 📁 Repository Structure
> maison-noir/
> > Backend/                 # Spring Boot application
> > > src/main/java/         # Java source code
> > > src/main/resources/    # Configuration & Database scripts
> > > pom.xml                # Maven build file
> > 
> > Frontend/                # React application
> > > src/                   # React source
> > > public/                # Static assets
> > > package.json           # npm dependencies
> > 
> > Dockerfile               # Multi‑stage Docker build
> > README.md                # This file

---

## 🚀 Getting Started (Local Development)

### Prerequisites
- **Java 24** (or 21+)
- **Maven** (included as wrapper)
- **Node.js 20.19+ or 22+** (CRITICAL: Vite 7 will fail on Node 18)
- **MySQL 8.0+** (Running on port 3306)
- **MongoDB 6.0+** (Running on port 27017, local or Atlas)
- **Git**

### 1. Clone the Repository
```bash
git clone https://github.com/swapnilsharma900/maison-noir.git
cd maison-noir
```

### 2. Configure Backend Databases
You do **not** need to manually create the MySQL database or run schema scripts. Spring Boot is configured to handle this automatically during startup.
1. Navigate to `Backend/src/main/resources/`.
2. Ensure your active properties file (e.g., `application-dev.properties`) contains the local database credentials.
*(See [Backend Architecture & Setup](Backend/README.md) for property details).*

### 3. Configure Frontend Environment
Create a `.env` file in the `Frontend/` directory (copy from `.env.example`):
```env
VITE_API_URL=http://localhost:8080
```

### 4. Build and Run the Backend
```bash
cd Backend
./mvnw clean spring-boot:run   # Linux/macOS
.\mvnw spring-boot:run        # Windows
```
The backend API will be available at `http://localhost:8080`.
Swagger UI: `http://localhost:8080/swagger-ui.html`

### 5. Build and Run the Frontend
Open a new terminal:
```bash
cd Frontend
npm install
npm run dev
```
The frontend development server will run on `http://localhost:5173`.

---

## 🐳 Docker Deployment
The project includes a multi‑stage Dockerfile that builds both frontend and backend into a single image. To build and run locally:
```bash
docker build -t maison-noir .
docker run -p 8080:8080 maison-noir
```

---

## 📖 API Documentation & Links
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Frontend App**: `http://localhost:5173`

---

## 📚 Detailed Documentation
- **[Backend Architecture & Setup](Backend/README.md):** Spring Boot configurations, application profiles (dev/prod).
- **[Database Architecture & Setup](Backend/src/main/resources/db/README.md):** Dual-database strategy and automated data seeding.
- **[Frontend Architecture & Documentation](Frontend/README.md):** React + Vite setup and styling guidelines.
