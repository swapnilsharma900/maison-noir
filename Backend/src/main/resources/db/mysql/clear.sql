-- ============================================================
-- MaisonNoir MySQL - Clear All Data
-- Run this BEFORE data.sql to start fresh
-- ============================================================

USE maison_noir;

-- Disable FK checks to allow truncation in any order
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE cart_items;
TRUNCATE TABLE carts;
TRUNCATE TABLE addresses;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

SELECT 'MySQL: All tables truncated successfully.' AS status;
