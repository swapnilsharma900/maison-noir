// ============================================================
// MaisonNoir MongoDB Seed Data
// Database: maison_noir
//
// Collections: products, items (product variants)
// ============================================================

use("maison_noir");

// Local demo images (repo: Data/Images/product_images/, served by Vite at /Data/Images/product_images/)
const IMG = "./Data/Images/product_images/";
const PRODUCT_IMAGES = {
  tee: IMG + "tshirt@maison-noir.png",
  hoodie: IMG + "hoodie@maison-noir.png",
  jogger: IMG + "jogger@maison-noir.png",
  cap: IMG + "midnight_cap@maison-noir.png",
  jacket: IMG + "jacket@maison-noir.png",
};

// ============================================================
// 1. PRODUCTS
// ============================================================
db.products.insertMany([
  {
    _id: ObjectId(),
    name: "Noir Classic Tee",
    description: "A premium cotton crew-neck t-shirt in deep obsidian black. Crafted from 100% organic cotton with a relaxed fit and subtle embossed Maison Noir logo on the chest.",
    category: "T-Shirts",
    images: [PRODUCT_IMAGES.tee],
    variants: [
      { label: "Size", values: ["S", "M", "L", "XL"] }
    ],
    attributes: {
      material: "100% Organic Cotton",
      fit: "Relaxed",
      care: "Machine wash cold, tumble dry low",
      weight: "180 GSM"
    },
    is_active: true,
    created_at: new Date("2026-01-15T10:00:00Z")
  },
  {
    _id: ObjectId(),
    name: "Shadow Hoodie",
    description: "An ultra-soft fleece hoodie with a matte finish. Features kangaroo pocket, drawstring hood, and ribbed cuffs. The perfect layering piece for the modern minimalist.",
    category: "Hoodies",
    images: [PRODUCT_IMAGES.hoodie],
    variants: [
      { label: "Size", values: ["S", "M", "L", "XL"] }
    ],
    attributes: {
      material: "80% Cotton, 20% Polyester Fleece",
      fit: "Regular",
      care: "Machine wash cold, do not bleach",
      weight: "320 GSM"
    },
    is_active: true,
    created_at: new Date("2026-01-20T10:00:00Z")
  },
  {
    _id: ObjectId(),
    name: "Eclipse Jogger",
    description: "Tailored jogger pants with a tapered leg and elastic waistband. Made from a premium cotton-spandex blend for all-day comfort without compromising style.",
    category: "Joggers",
    images: [PRODUCT_IMAGES.jogger],
    variants: [
      { label: "Size", values: ["S", "M", "L", "XL"] }
    ],
    attributes: {
      material: "95% Cotton, 5% Spandex",
      fit: "Tapered",
      care: "Machine wash cold, hang dry",
      weight: "260 GSM"
    },
    is_active: true,
    created_at: new Date("2026-02-01T10:00:00Z")
  },
  {
    _id: ObjectId(),
    name: "Midnight Cap",
    description: "Structured six-panel cap with embroidered Maison Noir monogram. Adjustable strap with metal buckle closure. One size fits all.",
    category: "Accessories",
    images: [PRODUCT_IMAGES.cap],
    variants: [],
    attributes: {
      material: "100% Cotton Twill",
      closure: "Metal Buckle",
      care: "Spot clean only"
    },
    is_active: true,
    created_at: new Date("2026-02-10T10:00:00Z")
  },
  {
    _id: ObjectId(),
    name: "Obsidian Jacket",
    description: "A sleek water-resistant bomber jacket with matte-black hardware. Lined with satin for a luxurious feel. Perfect transitional outerwear.",
    category: "Jackets",
    images: [PRODUCT_IMAGES.jacket],
    variants: [
      { label: "Size", values: ["S", "M", "L", "XL"] }
    ],
    attributes: {
      material: "100% Nylon, Satin Lining",
      fit: "Regular",
      care: "Dry clean only",
      waterResistant: true
    },
    is_active: true,
    created_at: new Date("2026-03-01T10:00:00Z")
  }
]);

// ============================================================
// 2. ITEMS (Product Variants / SKUs)
//
// After inserting products above, we need to fetch their _ids
// to link variants correctly.
// ============================================================

const noirTee       = db.products.findOne({ name: "Noir Classic Tee" })._id;
const shadowHoodie  = db.products.findOne({ name: "Shadow Hoodie" })._id;
const eclipseJogger = db.products.findOne({ name: "Eclipse Jogger" })._id;
const midnightCap   = db.products.findOne({ name: "Midnight Cap" })._id;
const obsidianJacket = db.products.findOne({ name: "Obsidian Jacket" })._id;

db.items.insertMany([
  // --- Noir Classic Tee Variants ---
  {
    product_id: noirTee.toString(),
    variant_label: "S",
    name: "Noir Classic Tee - S",
    image: PRODUCT_IMAGES.tee,
    price: NumberDecimal("2999.00"),
    category: "T-Shirts",
    stock_count: 25,
    is_available: true
  },
  {
    product_id: noirTee.toString(),
    variant_label: "M",
    name: "Noir Classic Tee - M",
    image: PRODUCT_IMAGES.tee,
    price: NumberDecimal("2999.00"),
    category: "T-Shirts",
    stock_count: 40,
    is_available: true
  },
  {
    product_id: noirTee.toString(),
    variant_label: "L",
    name: "Noir Classic Tee - L",
    image: PRODUCT_IMAGES.tee,
    price: NumberDecimal("2999.00"),
    category: "T-Shirts",
    stock_count: 35,
    is_available: true
  },
  {
    product_id: noirTee.toString(),
    variant_label: "XL",
    name: "Noir Classic Tee - XL",
    image: PRODUCT_IMAGES.tee,
    price: NumberDecimal("2999.00"),
    category: "T-Shirts",
    stock_count: 15,
    is_available: true
  },

  // --- Shadow Hoodie Variants ---
  {
    product_id: shadowHoodie.toString(),
    variant_label: "S",
    name: "Shadow Hoodie - S",
    image: PRODUCT_IMAGES.hoodie,
    price: NumberDecimal("2499.00"),
    category: "Hoodies",
    stock_count: 20,
    is_available: true
  },
  {
    product_id: shadowHoodie.toString(),
    variant_label: "M",
    name: "Shadow Hoodie - M",
    image: PRODUCT_IMAGES.hoodie,
    price: NumberDecimal("2499.00"),
    category: "Hoodies",
    stock_count: 30,
    is_available: true
  },
  {
    product_id: shadowHoodie.toString(),
    variant_label: "L",
    name: "Shadow Hoodie - L",
    image: PRODUCT_IMAGES.hoodie,
    price: NumberDecimal("2499.00"),
    category: "Hoodies",
    stock_count: 25,
    is_available: true
  },
  {
    product_id: shadowHoodie.toString(),
    variant_label: "XL",
    name: "Shadow Hoodie - XL",
    image: PRODUCT_IMAGES.hoodie,
    price: NumberDecimal("2499.00"),
    category: "Hoodies",
    stock_count: 10,
    is_available: true
  },

  // --- Eclipse Jogger Variants ---
  {
    product_id: eclipseJogger.toString(),
    variant_label: "S",
    name: "Eclipse Jogger - S",
    image: PRODUCT_IMAGES.jogger,
    price: NumberDecimal("2499.00"),
    category: "Joggers",
    stock_count: 18,
    is_available: true
  },
  {
    product_id: eclipseJogger.toString(),
    variant_label: "M",
    name: "Eclipse Jogger - M",
    image: PRODUCT_IMAGES.jogger,
    price: NumberDecimal("2499.00"),
    category: "Joggers",
    stock_count: 22,
    is_available: true
  },
  {
    product_id: eclipseJogger.toString(),
    variant_label: "L",
    name: "Eclipse Jogger - L",
    image: PRODUCT_IMAGES.jogger,
    price: NumberDecimal("2499.00"),
    category: "Joggers",
    stock_count: 28,
    is_available: true
  },
  {
    product_id: eclipseJogger.toString(),
    variant_label: "XL",
    name: "Eclipse Jogger - XL",
    image: PRODUCT_IMAGES.jogger,
    price: NumberDecimal("2499.00"),
    category: "Joggers",
    stock_count: 12,
    is_available: true
  },

  // --- Midnight Cap (single variant) ---
  {
    product_id: midnightCap.toString(),
    variant_label: "One Size",
    name: "Midnight Cap",
    image: PRODUCT_IMAGES.cap,
    price: NumberDecimal("1500.00"),
    category: "Accessories",
    stock_count: 50,
    is_available: true
  },

  // --- Obsidian Jacket Variants ---
  {
    product_id: obsidianJacket.toString(),
    variant_label: "S",
    name: "Obsidian Jacket - S",
    image: PRODUCT_IMAGES.jacket,
    price: NumberDecimal("7999.00"),
    category: "Jackets",
    stock_count: 8,
    is_available: true
  },
  {
    product_id: obsidianJacket.toString(),
    variant_label: "M",
    name: "Obsidian Jacket - M",
    image: PRODUCT_IMAGES.jacket,
    price: NumberDecimal("7999.00"),
    category: "Jackets",
    stock_count: 12,
    is_available: true
  },
  {
    product_id: obsidianJacket.toString(),
    variant_label: "L",
    name: "Obsidian Jacket - L",
    image: PRODUCT_IMAGES.jacket,
    price: NumberDecimal("7999.00"),
    category: "Jackets",
    stock_count: 10,
    is_available: true
  },
  {
    product_id: obsidianJacket.toString(),
    variant_label: "XL",
    name: "Obsidian Jacket - XL",
    image: PRODUCT_IMAGES.jacket,
    price: NumberDecimal("7999.00"),
    category: "Jackets",
    stock_count: 5,
    is_available: true
  }
]);

print("✅ MongoDB seed data inserted successfully!");
print("Products inserted: " + db.products.countDocuments());
print("Items (variants) inserted: " + db.items.countDocuments());
