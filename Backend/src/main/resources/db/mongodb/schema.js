// ============================================================
// MaisonNoir MongoDB Schema (Validation + Indexes)
// Database: maison_noir
// ============================================================

// Switch to the database
use("maison_noir");

// ============================================================
// 1. PRODUCTS COLLECTION
// ============================================================
db.createCollection("products", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["name", "category"],
      properties: {
        _id: {
          bsonType: "objectId",
          description: "Auto-generated ObjectId"
        },
        name: {
          bsonType: "string",
          description: "Product name - required"
        },
        description: {
          bsonType: "string",
          maxLength: 1000,
          description: "Product description (max 1000 chars)"
        },
        category: {
          bsonType: "string",
          description: "Product category - required, indexed"
        },
        images: {
          bsonType: "array",
          items: {
            bsonType: "string"
          },
          description: "List of image URLs"
        },
        variants: {
          bsonType: "array",
          items: {
            bsonType: "object",
            description: "Variant option metadata (e.g. {label: 'Size', values: ['S','M','L']})"
          },
          description: "Available axes of variation"
        },
        attributes: {
          bsonType: "object",
          description: "Flexible key-value attributes (e.g. material, care instructions)"
        },
        is_active: {
          bsonType: "bool",
          description: "Whether the product is active (default: true)"
        },
        created_at: {
          bsonType: "date",
          description: "Auto-generated creation timestamp"
        }
      }
    }
  }
});

// Indexes for products
db.products.createIndex({ name: 1 });
db.products.createIndex({ category: 1 });

// ============================================================
// 2. ITEMS (PRODUCT VARIANTS) COLLECTION
// ============================================================
db.createCollection("items", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["product_id", "name", "price", "stock_count"],
      properties: {
        _id: {
          bsonType: "objectId",
          description: "Auto-generated ObjectId"
        },
        product_id: {
          bsonType: "string",
          description: "Reference to parent product _id - required, indexed"
        },
        variant_label: {
          bsonType: "string",
          description: "Human-readable variant label (e.g. 'S', 'M', 'L', 'Red')"
        },
        name: {
          bsonType: "string",
          description: "Display name for this variant (e.g. 'Black Tee - Size M')"
        },
        image: {
          bsonType: "string",
          description: "Variant-specific image URL"
        },
        price: {
          bsonType: "decimal",
          description: "Selling price for this variant - must be positive"
        },
        category: {
          bsonType: "string",
          description: "Denormalized category from parent product"
        },
        stock_count: {
          bsonType: "int",
          minimum: 0,
          description: "Available inventory count - cannot be negative"
        },
        is_available: {
          bsonType: "bool",
          description: "Whether this variant is available for purchase (default: true)"
        }
      }
    }
  }
});

// Indexes for items
db.items.createIndex({ product_id: 1 });
