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

## 📁 Project Structure

> Frontend/
> 
> > public/
> > 
> > > ... # Static assets
> > 
> > src/
> > 
> > > components/ # Reusable UI components  
> > > contexts/ # Auth, Cart, etc.  
> > > pages/ # Route pages (Home, Shop, Login, etc.)  
> > > services/ # API service modules (auth, product, cart)  
> > > styles/ # Global CSS / Tailwind imports  
> > > App.jsx # Main app with routes  
> > > main.jsx # Entry point  
> > > vite.config.js # Vite configuration
> > 
> > .env # Environment variables (not committed)  
> > .env.example # Sample env file  
> > index.html  
> > package.json  
> > README.md # This file
> > 
> > vite.config.js

---

## 🚀 Getting Started

### Prerequisites

- Node.js 18+
- npm 9+ or yarn

### Installation

```bash
# Clone the repository (if not already done)
git clone https://github.com/swapnilsharma900/maison-noir.git
cd maison-noir/Frontend

# Install dependencies
npm install
```

### Environment Variables

Copy `.env.example` to `.env` and set the backend API URL:

```
# .env
VITE_API_URL=http://localhost:8080   # For development
# For production, this will be injected via Docker build arg
```

> **Note:** Vite requires variables to be prefixed with `VITE_` to be exposed in `import.meta.env`.

### Development Server

```bash
npm run dev
```

The app will be available at `http://localhost:5173`. The development server proxies API requests to the backend if configured in `vite.config.js`.

### Build for Production

```bash
npm run build
```

This generates a `dist/` folder with optimized static files ready for deployment.

### Preview Production Build

```bash
npm run preview
```

---

## 🧩 Key Features

- **Authentication**: Login/Register forms with JWT token storage.

- **Product Catalog**: Browse products with category filters.

- **Shopping Cart**: Add/remove items, update quantities.

- **Order Checkout**: Place orders (requires login).

- **Admin Dashboard**: Manage products, orders, users (admin only).

- **Responsive Design**: Works on mobile, tablet, and desktop.

---

## 🔗 API Integration

All API calls are centralized in `src/services/`. The base URL is determined by `import.meta.env.VITE_API_URL`. For production, this variable is injected during the Docker build (see main README).

Example usage:

```js
import { authService } from '../services/authService';

const response = await authService.login(email, password);
// response.data contains { token, role, ... }
```

## 🧪 Testing

Run tests (if configured):

```bash
npm test
```

---

## 📦 Deployment

The frontend is deployed alongside the backend via the Dockerfile at the project root. On Render, the frontend is built and copied to the backend’s static resources, so both are served from the same domain.

If deploying separately, you can host the `dist/` folder on any static hosting service (e.g., Netlify, Vercel, Render Static Site).

---

## 🛠️ Customization

### Tailwind CSS

Tailwind is configured in `tailwind.config.js`. Modify the theme or add plugins as needed.

### Material UI

MUI components are used alongside Tailwind. You can customize the theme in `src/theme.js`.

### Adding New Pages

1. Create a new component in `src/pages/`.

2. Add a route in `App.jsx` using React Router.

---

## 🤝 Contributing

Follow the same process as the main project. Ensure code quality and responsiveness.

---

## 📄 License

Proprietary – all rights reserved.
