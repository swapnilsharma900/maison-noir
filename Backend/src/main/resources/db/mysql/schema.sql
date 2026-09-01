-- ============================================================
-- MaisonNoir MySQL Schema
-- Database: maison_noir
-- ============================================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS maison_noir;
USE maison_noir;

-- ============================================================
-- 1. USERS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    first_name      VARCHAR(255)    NOT NULL,
    last_name       VARCHAR(255)    NOT NULL,
    email           VARCHAR(255)    NOT NULL,
    phone           VARCHAR(255),
    password        VARCHAR(255)    NOT NULL,
    role            ENUM('ADMIN', 'CUSTOMER') NOT NULL,
    created_at      DATETIME(6)     NOT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 2. ADDRESSES TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS addresses (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    line_one        VARCHAR(255)    NOT NULL,
    line_two        VARCHAR(255)    NOT NULL,
    landmark        VARCHAR(255),
    city            VARCHAR(255)    NOT NULL,
    state           VARCHAR(255)    NOT NULL,
    pincode         VARCHAR(255)    NOT NULL,
    country         VARCHAR(255)    NOT NULL,

    PRIMARY KEY (id),
    INDEX idx_addresses_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 3. CARTS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS carts (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    total_amount    DECIMAL(10,2)   NOT NULL DEFAULT 0.00,
    updated_at      DATETIME(6)     NOT NULL,

    PRIMARY KEY (id),
    INDEX idx_carts_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 4. CART_ITEMS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS cart_items (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    cart_id             BIGINT          NOT NULL,
    variant_id          VARCHAR(255)    NOT NULL,   -- References MongoDB items._id
    quantity            INT             NOT NULL,
    snapshot_name       VARCHAR(255),
    snapshot_image      VARCHAR(255),
    snapshot_price      DECIMAL(10,2),
    variant_label       VARCHAR(255),
    snapshot_category   VARCHAR(255),

    PRIMARY KEY (id),
    INDEX idx_cart_items_cart_id (cart_id),
    CONSTRAINT fk_cart_items_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 5. ORDERS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS orders (
    id              BIGINT          NOT NULL AUTO_INCREMENT,
    user_id         BIGINT          NOT NULL,
    ship_name       VARCHAR(255)    NOT NULL,
    ship_flat       VARCHAR(255)    NOT NULL,
    ship_city       VARCHAR(255)    NOT NULL,
    ship_pincode    VARCHAR(255)    NOT NULL,
    order_status    ENUM('PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED', 'RETURNED') NOT NULL DEFAULT 'PENDING',
    total           DECIMAL(10,2)   NOT NULL,
    payment_status  ENUM('PENDING', 'PAID', 'FAILED') NOT NULL DEFAULT 'PENDING',
    payment_method  VARCHAR(255)    NOT NULL,
    placed_at       DATETIME(6)     NOT NULL,
    updated_at      DATETIME(6)     NOT NULL,
    version         BIGINT          DEFAULT 0,

    PRIMARY KEY (id),
    INDEX idx_order_user_id (user_id),
    INDEX idx_order_status (order_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- 6. ORDER_ITEMS TABLE
-- ============================================================
CREATE TABLE IF NOT EXISTS order_items (
    id                  BIGINT          NOT NULL AUTO_INCREMENT,
    order_id            BIGINT          NOT NULL,
    variant_id          VARCHAR(255)    NOT NULL,   -- References MongoDB items._id
    quantity            INT             NOT NULL,
    snapshot_name       VARCHAR(255),
    snapshot_image      VARCHAR(255),
    snapshot_price      DECIMAL(10,2),
    variant_label       VARCHAR(255),
    snapshot_category   VARCHAR(255),

    PRIMARY KEY (id),
    INDEX idx_order_items_order_id (order_id),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
