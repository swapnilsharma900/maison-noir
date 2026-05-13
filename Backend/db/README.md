# MaisonNoir Database Scripts

## Directory Structure

```
db/
├── mysql/
│   ├── schema.sql    # MySQL table definitions (DDL)
│   ├── data.sql      # MySQL seed data (DML)
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

### 1. Clear existing data
```powershell
# MySQL
Get-Content db\mysql\clear.sql | mysql -u root -p maison_noir

# MongoDB
mongosh --file db\mongodb\clear.js
```

### 2. Seed fresh data
```powershell
# MySQL (run first)
Get-Content db\mysql\data.sql | mysql -u root -p maison_noir

# MongoDB (run second)
mongosh --file db\mongodb\data.js
```

### 3. Sync variant IDs
After seeding MongoDB, the `variant_id` values in MySQL `cart_items` and `order_items` tables use **placeholder strings** (e.g. `VARIANT_NOIR_TEE_M`). These need to be updated with the actual MongoDB `_id` values from the `items` collection.

You can get the MongoDB IDs with:
```powershell
mongosh --eval "use('maison_noir'); db.items.find({}, {_id:1, name:1}).forEach(doc => print(doc._id + ' => ' + doc.name));"
```

Then update MySQL accordingly.


## Important Notes

- MySQL `cart_items.variant_id` and `order_items.variant_id` reference MongoDB `items._id`
- The `ddl-auto=update` setting in `application.properties` means Hibernate will auto-create/update tables, so `schema.sql` is primarily for documentation and manual setup
- MongoDB schema validation is optional but recommended for data integrity
