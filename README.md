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
> 
> > Backend/ # Spring Boot application
> > 
> > > src/main/java/ # Java source code
> > > 
> > > src/main/resources/ # Production Config
> > > 
> > > pom.xml # Maven build file
> > 
> > Frontend/ # React application
> > 
> > > src/ # React source
> > > 
> > > public/ # Static assets
> > > 
> > > package.json # npm dependencies
> > 
> > Dockerfile # Multi‑stage Docker build
> > 
> > README.md # This file

---

## 🚀 Getting Started (Local Development)

### Prerequisites

- **Java 24** (or 21+)

- **Maven** (included as wrapper)

- **Node.js** 18+ and **npm** 9+

- **MySQL** 8.0+

- **MongoDB** 6.0+ (local or Atlas)

- **Git**
  
  ### 1. Clone the Repository
  
  ```bash
  git clone https://github.com/swapnilsharma900/maison-noir.git
  cd maison-noir
  ```

### 2. Set Up Databases

        Create two databases in MySQL and MongoDB:

- **MySQL** – database name: `maison_noir`

- **MongoDB** – database name: `maison_noir`

Refer to [Database Documentation](Backend/src/main/resources/db/README.md) for seeding initial data.

### 3. Configure Environment Variables

Create a `.env` file in the `Frontend/` directory (or set in Render’s dashboard):

`VITE_API_URL = http://localhost:8080   # or your backend URL`

Refer to [Frontend Documentation](Frontend/README.md) for detailed setup

### 4. Build and Run the Backend

```cd
cd Backend
./mvnw clean spring-boot:run   # Linux/macOS
.\mvnw spring-boot:run         # Windows
```

The backend API will be available at `http://localhost:8080`.  
Swagger UI: `http://localhost:8080/swagger-ui.html`

For more detail about Project Backend refer to [Backend Documentation](Backend/README.md)

### 5. Build and Run the Frontend

Open a new terminal:

```
cd Frontend
npm install
npm run dev
```

The frontend development server will run on `http://localhost:5173`.

---

## 🐳 Docker Deployment

The project includes a multi‑stage Dockerfile that builds both frontend and backend into a single image. To build and run locally:

```
docker build -t maison-noir .
docker run -p 8080:8080 maison-noir
```

For production deployment (Render), the Dockerfile is used with Render’s build environment variables.

---

## 🧪 Testing

- **Backend tests** – run `./mvnw test` in the `Backend/` directory.

---

## 📖 API Documentation

Once the backend is running, explore the API interactively via Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

---

## 🤝 Contributing

1. Fork the repository.

2. Create a feature branch (`git checkout -b feature/amazing-feature`).

3. Commit your changes (`git commit -m 'Add some amazing feature'`).

4. Push to the branch (`git push origin feature/amazing-feature`).

5. Open a Pull Request.

---

## 📄 License

This project is proprietary – all rights reserved.

---

## 📬 Contact

**Swapnil Sharma** – [GitHub](https://github.com/swapnilsharma900)

**Project Link** – [GitHub - swapnilsharma900/maison-noir](https://github.com/swapnilsharma900/maison-noir)

---

## 🙏 Acknowledgements

- Spring Boot & React communities

- Render for seamless deployment

- MongoDB & MySQL for powerful data storage

---

## Project Structure & Documentation

- **[Backend Architecture & Setup](Backend/README.md):** Information on the Spring Boot backend, APIs, and how to run the server.
- **[Database Architecture & Setup](Backend/src/main/resources/db/README.md):** Information on our dual-database strategy {Mysql + MongoDB} and seeding scripts.
- **[Frontend Architecture & Documentation](Frontend/README.md):** Information on the React + Vite frontend with Tailwind CSS Styling and how to setup & run the frontend server.
