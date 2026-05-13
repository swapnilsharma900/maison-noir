// ============================================================
// MaisonNoir MongoDB - Clear All Data
// Run this BEFORE data.js to start fresh
// ============================================================

use("maison_noir");

db.products.deleteMany({});
db.items.deleteMany({});

print("✅ MongoDB: All collections cleared successfully.");
print("Products remaining: " + db.products.countDocuments());
print("Items remaining: " + db.items.countDocuments());
