-- Включаем расширение для генерации UUID
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE SCHEMA IF NOT EXISTS "shopping_cart";

CREATE TABLE IF NOT EXISTS shopping_cart.carts (
    cart_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    username varchar(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS shopping_cart.cart_products (
    cart_id uuid NOT NULL REFERENCES shopping_cart.carts(cart_id) ON DELETE CASCADE,
    product_id uuid NOT NULL,
    quantity int NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (cart_id, product_id)
);