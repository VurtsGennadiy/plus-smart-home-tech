CREATE SCHEMA IF NOT EXISTS commerce_delivery;

CREATE TABLE IF NOT EXISTS commerce_delivery.addresses (
    address_id UUID PRIMARY KEY,
    country VARCHAR(30),
    city VARCHAR(30),
    street VARCHAR(30),
    house VARCHAR(30),
    flat VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS commerce_delivery.deliveries (
    delivery_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'CREATED',
    from_address_id UUID REFERENCES commerce_delivery.addresses(address_id) ON DELETE RESTRICT,
    to_address_id UUID REFERENCES commerce_delivery.addresses(address_id) ON DELETE RESTRICT,
    delivery_weight DOUBLE PRECISION NOT NULL,
    delivery_volume DOUBLE PRECISION NOT NULL,
    fragile BOOLEAN NOT NULL
);

