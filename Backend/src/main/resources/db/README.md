# Maison Noir Database Setup

This directory contains scripts and documentation for the dual‑database architecture (MySQL + MongoDB).

## 📂 Directory Structure

> Backend/src/main/resources/db/
> 
> > mysql/
> > 
> > > schema.sql # MySQL table definitions (DDL) – for reference
> > > 
> > > data.sql # MySQL seed data (DML) – loaded automatically on startup
> > > 
> > > clear.sql # Truncate all MySQL tables
> > 
> > mongodb/
> > 
> > > schema.js # MongoDB collection validation & indexes
> > > 
> > > data.js # MongoDB seed data (products & variants)
> > > 
> > > clear.js # Clear all MongoDB collections
> > 
> > README.md # This file

---

## 🗄️ Database Overview

| Database                    | Purpose                         | Main Tables/Collections                                              |
| --------------------------- | ------------------------------- | -------------------------------------------------------------------- |
| **MySQL** (`maison_noir`)   | Users, addresses, carts, orders | `users`, `addresses`, `carts`, `cart_items`, `orders`, `order_items` |
| **MongoDB** (`maison_noir`) | Product catalog & variants      | `products`, `items` (product variants)                               |

> **Important:** `cart_items.variant_id` and `order_items.variant_id` in MySQL store **MongoDB `items._id` strings** – this is the link between the two databases.

---

## 🚀 Automatic Seeding on Startup

The backend is configured to automatically run `mysql/data.sql` on startup:

```properties
# application.properties
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:db/mysql/data.sql
spring.jpa.defer-datasource-initialization=true
```

This ensures fresh seed data each time the application restarts (useful for development).

---

## 🔄 How to Reset & Seed Data - Manually

Run commands from the **repository root** (`maison-noir/`).

### 1. Clear Existing Data

#### MySQL

```bash
# Linux/macOS
mysql -u root -p maison_noir < Backend/src/main/resources/db/mysql/clear.sql

# Windows (PowerShell)
Get-Content Backend\src\main\resources\db\mysql\clear.sql | mysql -u root -p maison_noir
```

#### MongoDB

```bash
# Linux/macOS
mongosh --file Backend/src/main/resources/db/mongodb/clear.js

# Windows (PowerShell)
mongosh --file Backend\src\main\resources\db\mongodb\clear.js
```

### 2. Seed Fresh Data (Run in Order)

#### MySQL (First)

```bash
# Linux/macOS
mysql -u root -p maison_noir < Backend/src/main/resources/db/mysql/data.sql

# Windows (PowerShell)
Get-Content Backend\src\main\resources\db\mysql\data.sql | mysql -u root -p maison_noir
```

#### MongoDB (Second)

```bash
# Linux/macOS
mongosh --file Backend/src/main/resources/db/mongodb/data.js

# Windows (PowerShell)
mongosh --file Backend\src\main\resources\db\mongodb\data.js
```

### 3. Sync Variant IDs (MySQL ↔ MongoDB)

After seeding, the `variant_id` columns in `cart_items` and `order_items` contain **placeholder strings** (e.g., `VARIANT_NOIR_TEE_M`). You must replace these with the actual MongoDB `_id` values from the `items` collection.

To get the mapping, run:

```javascript
// In mongosh
use('maison_noir');
db.items.find({}, {_id: 1, name: 1}).forEach(doc => print(doc._id + ' => ' + doc.name));
```

Then update MySQL manually (or script it) to replace the placeholders with the real IDs.

---

## 🧪 Validation & Indexes

- MySQL schema is managed by Hibernate (`ddl-auto=update`) – `schema.sql` is only for reference.

- MongoDB schema validation and indexes are defined in `schema.js` – run this once to apply them.

To apply MongoDB schema validation:

```bash
mongosh --file Backend/src/main/resources/db/mongodb/schema.js
```

---

## ❓ Troubleshooting

- **“Access denied”** – verify MySQL credentials and that the database `maison_noir` exists.

- **MongoDB connection refused** – ensure MongoDB is running and the URI is correct.

- **Variant ID mismatch** – after seeding, always sync the placeholder IDs with actual MongoDB `_id`s.

---

## 📄 License

Proprietary – all rights reserved.
