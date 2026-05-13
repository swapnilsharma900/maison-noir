-- ============================================================
-- MaisonNoir MySQL Seed Data
-- Database: maison_noir
--
-- ADMIN Credentials:
--   Email:    admin@maisonnoir.in
--   Password: Admin@123
--
-- CUSTOMER Credentials:
--   Email:    rahul.sharma@gmail.com   | Password: Customer@123
--   Email:    priya.patel@gmail.com    | Password: Customer@123
--   Email:    arjun.kumar@gmail.com    | Password: Customer@123
-- ============================================================

-- ============================================================
-- 1. USERS (1 Admin + 3 Customers)
-- Passwords are BCrypt encoded
-- Admin@123  => $2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36Kz2G4HgCL.H.xV7qqDqVK
-- Customer@123 => $2a$10$dXJ3SW6G7P50lGmMQGeUs.OUehR.KOGXb0zfY4ZfOp1cJoJXbP7qC
-- ============================================================
INSERT IGNORE INTO users (first_name, last_name, email, phone, password, role, created_at) VALUES
('Maison',  'Admin',    'admin@maisonnoir.in',      '+919876543210', '$2a$10$NUG6ejwNJz5/nnnBZr949eYILPdD8Ird/kcVhasDMnK24Bpe8CsGi', 'ADMIN',    NOW()),
('Rahul',   'Sharma',   'rahul.sharma@gmail.com',   '+919812345678', '$2a$10$a2C7fQpTp9mMsxd3jSgAKOQtYXEYScfEiPHMtuKXH7auoww.nk6ta', 'CUSTOMER', NOW()),
('Priya',   'Patel',    'priya.patel@gmail.com',    '+919823456789', '$2a$10$a2C7fQpTp9mMsxd3jSgAKOQtYXEYScfEiPHMtuKXH7auoww.nk6ta', 'CUSTOMER', NOW()),
('Arjun',   'Kumar',    'arjun.kumar@gmail.com',    '+919834567890', '$2a$10$a2C7fQpTp9mMsxd3jSgAKOQtYXEYScfEiPHMtuKXH7auoww.nk6ta', 'CUSTOMER', NOW());

-- ============================================================
-- 2. ADDRESSES
-- ============================================================
INSERT IGNORE INTO addresses (user_id, line_one, line_two, landmark, city, state, pincode, country) VALUES
(1, 'Office 12A',          'MaisonNoir Tower, Sector 62',  'Near Metro Station',       'Noida',        'Uttar Pradesh',    '201301', 'India'),
(2, 'Flat 301, Wing B',    'Sunrise Apartments, Viman Nagar', 'Opposite Phoenix Mall',  'Pune',         'Maharashtra',      '411014', 'India'),
(2, '42, Ground Floor',    'Green Park Colony',            'Near City Hospital',       'Delhi',         'Delhi',            '110016', 'India'),
(3, '15, Lakeview Villa',  'Whitefield Main Road',         'Behind Forum Mall',        'Bangalore',     'Karnataka',        '560066', 'India'),
(4, 'House No. 78',        'Jubilee Hills, Road No. 36',   'Near KBR Park',            'Hyderabad',     'Telangana',        '500033', 'India');

-- ============================================================
-- 3. CARTS (1 per customer, admin gets one too)
-- ============================================================
INSERT IGNORE INTO carts (user_id, total_amount, updated_at) VALUES
(1, 0.00,     NOW()),
(2, 5998.00,  NOW()),
(3, 2499.00,  NOW()),
(4, 0.00,     NOW());

-- ============================================================
-- 4. CART_ITEMS
-- NOTE: variant_id values must match MongoDB items._id
--       Using placeholder ObjectId strings that must be replaced
--       with actual MongoDB _id values after MongoDB seeding
-- ============================================================
INSERT IGNORE INTO cart_items (cart_id, variant_id, quantity, snapshot_name, snapshot_image, snapshot_price, variant_label, snapshot_category) VALUES
-- Rahul's cart (cart_id=2): 2 items
(2, 'VARIANT_NOIR_TEE_M',      1, 'Noir Classic Tee - M',       'https://placehold.co/400x500/1a1a1a/ffffff?text=Noir+Tee',     2999.00, 'M', 'T-Shirts'),
(2, 'VARIANT_NOIR_TEE_L',      1, 'Noir Classic Tee - L',       'https://placehold.co/400x500/1a1a1a/ffffff?text=Noir+Tee',     2999.00, 'L', 'T-Shirts'),
-- Priya's cart (cart_id=3): 1 item
(3, 'VARIANT_SHADOW_HOODIE_S', 1, 'Shadow Hoodie - S',          'https://placehold.co/400x500/2d2d2d/ffffff?text=Shadow+Hoodie', 2499.00, 'S', 'Hoodies');

-- ============================================================
-- 5. ORDERS
-- ============================================================
INSERT IGNORE INTO orders (user_id, ship_name, ship_flat, ship_city, ship_pincode, order_status, total, payment_status, payment_method, placed_at, updated_at, version) VALUES
-- Rahul's delivered order
(2, 'Rahul Sharma',  'Flat 301, Wing B, Sunrise Apartments', 'Pune',      '411014', 'DELIVERED',  4499.00, 'PAID',    'UPI',  '2026-04-15 10:30:00', '2026-04-20 14:00:00', 0),
-- Priya's confirmed order
(3, 'Priya Patel',   '15, Lakeview Villa, Whitefield',       'Bangalore', '560066', 'CONFIRMED',  7497.00, 'PAID',    'CARD', '2026-05-01 18:45:00', '2026-05-02 09:00:00', 0),
-- Arjun's pending order
(4, 'Arjun Kumar',   'House No. 78, Jubilee Hills',          'Hyderabad', '500033', 'PENDING',    2999.00, 'PENDING', 'COD',  '2026-05-08 22:15:00', '2026-05-08 22:15:00', 0),
-- Rahul's cancelled order
(2, 'Rahul Sharma',  '42, Ground Floor, Green Park Colony',  'Delhi',     '110016', 'CANCELLED',  4998.00, 'FAILED',  'CARD', '2026-04-25 08:00:00', '2026-04-26 10:00:00', 0);

-- ============================================================
-- 6. ORDER_ITEMS
-- NOTE: variant_id values must match MongoDB items._id
-- ============================================================
INSERT IGNORE INTO order_items (order_id, variant_id, quantity, snapshot_name, snapshot_image, snapshot_price, variant_label, snapshot_category) VALUES
-- Rahul's delivered order (order_id=1)
(1, 'VARIANT_NOIR_TEE_M',          1, 'Noir Classic Tee - M',       'https://placehold.co/400x500/1a1a1a/ffffff?text=Noir+Tee',           2999.00, 'M',     'T-Shirts'),
(1, 'VARIANT_MIDNIGHT_CAP',        1, 'Midnight Cap',               'https://placehold.co/400x500/111111/ffffff?text=Midnight+Cap',        1500.00, 'One Size', 'Accessories'),
-- Priya's confirmed order (order_id=2)
(2, 'VARIANT_SHADOW_HOODIE_M',     1, 'Shadow Hoodie - M',          'https://placehold.co/400x500/2d2d2d/ffffff?text=Shadow+Hoodie',       2499.00, 'M',     'Hoodies'),
(2, 'VARIANT_SHADOW_HOODIE_L',     1, 'Shadow Hoodie - L',          'https://placehold.co/400x500/2d2d2d/ffffff?text=Shadow+Hoodie',       2499.00, 'L',     'Hoodies'),
(2, 'VARIANT_NOIR_TEE_S',          1, 'Noir Classic Tee - S',       'https://placehold.co/400x500/1a1a1a/ffffff?text=Noir+Tee',           2499.00, 'S',     'T-Shirts'),
-- Arjun's pending order (order_id=3)
(3, 'VARIANT_NOIR_TEE_L',          1, 'Noir Classic Tee - L',       'https://placehold.co/400x500/1a1a1a/ffffff?text=Noir+Tee',           2999.00, 'L',     'T-Shirts'),
-- Rahul's cancelled order (order_id=4)
(4, 'VARIANT_ECLIPSE_JOGGER_M',    1, 'Eclipse Jogger - M',         'https://placehold.co/400x500/333333/ffffff?text=Eclipse+Jogger',      2499.00, 'M',     'Joggers'),
(4, 'VARIANT_ECLIPSE_JOGGER_L',    1, 'Eclipse Jogger - L',         'https://placehold.co/400x500/333333/ffffff?text=Eclipse+Jogger',      2499.00, 'L',     'Joggers');
