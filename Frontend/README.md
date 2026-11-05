# Maison Noir Frontend

This directory contains the React frontend for the Maison Noir e‑commerce platform.

---

## 🖥️ Tech Stack
- **Library**: React 19
- **Build Tool**: Vite 7
- **Styling**: Tailwind CSS 4, Material UI (MUI)
- **Routing**: React Router v6
- **HTTP Client**: Custom API wrapper (fetch)
- **State Management**: React Context API (Auth, Cart)

---

## 🚀 Getting Started

### Prerequisites
- **Node.js 20.19+ or 22+** (CRITICAL: Vite 7 requires modern Node versions. It will crash with `crypto.hash is not a function` on Node 18).
- npm 9+

### Installation
```bash
cd maison-noir/Frontend

# Install dependencies
npm install
```

### Environment Variables
Copy `.env.example` to `.env` and set the backend API URL:
```env
# .env
VITE_API_URL=http://localhost:8080
```
> **Note:** Vite requires variables to be prefixed with `VITE_` to be exposed in `import.meta.env`.

### Development Server
```bash
npm run dev
```
The app will be available at `http://localhost:5173`. 

---

## 📦 Deployment
The frontend is deployed alongside the backend via the Dockerfile at the project root. On Render, the frontend is built and copied to the backend’s static resources (`src/main/resources/static`), so both are served from the same domain.

During Docker builds, ensure `VITE_API_URL` is passed as a build argument if it differs from the origin.

---

## 🔗 API Integration
All API calls are centralized in `src/services/`. The base URL is determined by `import.meta.env.VITE_API_URL`.

Example usage:
```js
import { authService } from '../services/authService';

const response = await authService.login(email, password);
// response.data contains { token, role, ... }
```
