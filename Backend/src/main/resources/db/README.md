# MaisonNoir Database Scripts

Scripts live at **`Backend/src/main/resources/db/`** (on the Spring Boot classpath as `classpath:db/...`).

## Directory Structure

```
Backend/src/main/resources/db/
├── mysql/
│   ├── schema.sql    # MySQL table definitions (DDL)
│   ├── data.sql      # MySQL seed data (DML) — also loaded on app startup
│   └── clear.sql     # Truncate all MySQL tables
├── mongodb/
│   ├── schema.js     # MongoDB collection validation + indexes
│   ├── data.js       # MongoDB seed data (products & variants)
│   └── clear.js      # Clear all MongoDB collections
└── README.md         # This file
```

## Database Overview

| Database | Purpose | Tables/Collections |
|----------|---------|-------------------|
| **MySQL** (`maison_noir`) | Users, Addresses, Carts, Orders | `users`, `addresses`, `carts`, `cart_items`, `orders`, `order_items` |
| **MongoDB** (`maison_noir`) | Product Catalog | `products`, `items` (product variants) |

## How to Reset & Seed

Run commands from the **repository root** (`maison-noir/`).

### 1. Clear existing data

```powershell
# Windows — MySQL
Get-Content Backend\src\main\resources\db\mysql\clear.sql | mysql -u root -p maison_noir

# Windows — MongoDB
mongosh --file Backend\src\main\resources\db\mongodb\clear.js
```

```bash
# Linux/macOS — MySQL
mysql -u root -p maison_noir < Backend/src/main/resources/db/mysql/clear.sql

# Linux/macOS — MongoDB
mongosh --file Backend/src/main/resources/db/mongodb/clear.js
```

### 2. Seed fresh data

```powershell
# Windows — MySQL (run first)
Get-Content Backend\src\main\resources\db\mysql\data.sql | mysql -u root -p maison_noir

# Windows — MongoDB (run second)
mongosh --file Backend\src\main\resources\db\mongodb\data.js
```

```bash
# Linux/macOS — MySQL (run first)
mysql -u root -p maison_noir < Backend/src/main/resources/db/mysql/data.sql

# Linux/macOS — MongoDB (run second)
mongosh --file Backend/src/main/resources/db/mongodb/data.js
```

### 3. Sync variant IDs

After seeding MongoDB, the `variant_id` values in MySQL `cart_items` and `order_items` tables use **placeholder strings** (e.g. `VARIANT_NOIR_TEE_M`). These need to be updated with the actual MongoDB `_id` values from the `items` collection.

You can get the MongoDB IDs with:

```powershell
mongosh --eval "use('maison_noir'); db.items.find({}, {_id:1, name:1}).forEach(doc => print(doc._id + ' => ' + doc.name));"
```

Then update MySQL accordingly.

## Automatic MySQL seed on startup

`Backend/src/main/resources/application.properties` configures:

```properties
spring.sql.init.mode=always
spring.sql.init.data-locations=classpath:db/mysql/data.sql
spring.jpa.defer-datasource-initialization=true
```

Hibernate applies schema first (`ddl-auto=update`), then `data.sql` runs from this directory on the classpath.

## Important Notes

- MySQL `cart_items.variant_id` and `order_items.variant_id` reference MongoDB `items._id`
- The `ddl-auto=update` setting in `application.properties` means Hibernate will auto-create/update tables, so `schema.sql` is primarily for documentation and manual setup
- MongoDB schema validation is optional but recommended for data integrity
