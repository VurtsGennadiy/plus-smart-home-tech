CREATE SCHEMA IF NOT EXISTS "commerce_order";

-- Таблица адресов для доставки
CREATE TABLE IF NOT EXISTS commerce_order.addresses (
    address_id UUID PRIMARY KEY,
    country VARCHAR(30),
    city VARCHAR(30),
    street VARCHAR(30),
    house VARCHAR(30),
    flat VARCHAR(30)
);

-- Таблица заказов
CREATE TABLE IF NOT EXISTS commerce_order.orders (
    order_id UUID PRIMARY KEY,
    state VARCHAR(20) NOT NULL DEFAULT 'NEW',
    username VARCHAR(50) NOT NULL,
    cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    delivery_address_id UUID NOT NULL,
    delivery_weight DOUBLE PRECISION NOT NULL,
    delivery_volume DOUBLE PRECISION NOT NULL,
    fragile BOOLEAN NOT NULL,
    total_price DECIMAL,
    product_price DECIMAL,
    delivery_price DECIMAL,
    FOREIGN KEY (delivery_address_id) REFERENCES commerce_order.addresses(address_id) ON DELETE RESTRICT
);

-- Таблица продуктов в заказе
CREATE TABLE IF NOT EXISTS commerce_order.order_products (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES commerce_order.orders(order_id) ON DELETE CASCADE
);
