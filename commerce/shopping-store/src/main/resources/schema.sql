CREATE SCHEMA IF NOT EXISTS "shopping_store";

CREATE TABLE IF NOT EXISTS "shopping_store".products (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR,
    image_src VARCHAR(255),
    category VARCHAR(30) NOT NULL,
    quantity_state VARCHAR(30) NOT NULL,
    product_state VARCHAR(30) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);