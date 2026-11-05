# Maison Noir Database Setup

This directory contains scripts and documentation for the dual‑database architecture (MySQL + MongoDB).

## 🗄️ Database Overview
| Database                    | Purpose                         | Main Tables/Collections                                              |
| --------------------------- | ------------------------------- | -------------------------------------------------------------------- |
| **MySQL** (`maison_noir`)   | Users, addresses, carts, orders | `users`, `addresses`, `carts`, `cart_items`, `orders`, `order_items` |
| **MongoDB** (`maison_noir`) | Product catalog & variants      | `products`, `items` (product variants)                               |

> **Important:** `cart_items.variant_id` and `order_items.variant_id` in MySQL store **MongoDB `items._id` strings** – this is the link between the two databases.

---

## 🚀 Automated Seeding on Startup (No Manual Scripts Required)

To eliminate the friction of manually running `schema.sql`, `schema.js`, and mapping Variant IDs between databases, the Maison Noir backend is configured to **automatically initialize and seed data** when running the `dev` profile.

### 1. MySQL Automation
When using `application-dev.properties`, the following properties ensure MySQL is created and populated automatically:
```properties
# Creates the DB if missing
spring.datasource.url=jdbc:mysql://localhost:3306/maison_noir?createDatabaseIfNotExist=true

# Automatically runs schema.sql and data.sql
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
```

### 2. MongoDB Automation & Variant ID Syncing
Instead of manually running Mongo shell scripts (`data.js`) and copying ObjectIDs to MySQL, the project utilizes a Spring Boot `CommandLineRunner` (e.g., `DatabaseSeeder.java`). 

On startup, this component:
1. Checks if the MongoDB `products` collection is empty.
2. If empty, it inserts the default luxury products into MongoDB.
3. It retrieves the newly generated MongoDB `_id`s.
4. It updates the placeholder IDs (e.g., `VARIANT_NOIR_TEE_M`) in the MySQL `cart_items` and `order_items` tables with the real MongoDB `_id`s.

*This guarantees that your relational constraints and NoSQL documents are perfectly synced every time you spin up a fresh development environment.*

---

## 🔄 Manual Reset (If Needed)
If you ever need to completely wipe the databases and let Spring Boot re-seed them from scratch on the next boot:

**MySQL (Wipe via terminal):**
```bash
mysql -u root -p -e "DROP DATABASE maison_noir;"
```

**MongoDB (Wipe via mongosh):**
```bash
mongosh mongodb://localhost:27017/maison_noir --eval "db.dropDatabase()"
```
*Restart the Spring Boot application, and both databases will be recreated and synchronized seamlessly.*
