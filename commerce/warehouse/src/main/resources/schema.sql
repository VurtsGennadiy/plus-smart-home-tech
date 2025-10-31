CREATE SCHEMA IF NOT EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse.product_attributes (
    product_id UUID PRIMARY KEY,
    width DOUBLE PRECISION NOT NULL,
    height DOUBLE PRECISION NOT NULL,
    depth DOUBLE PRECISION NOT NULL,
    weight DOUBLE PRECISION NOT NULL,
    fragile BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS warehouse.addresses (
    address_id SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    country VARCHAR(30),
    city VARCHAR(30),
    street VARCHAR(30),
    house VARCHAR(30),
    flat VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS warehouse.stocks (
    product_id UUID REFERENCES warehouse.product_attributes(product_id) ON DELETE RESTRICT,
    address_id SMALLINT REFERENCES warehouse.addresses(address_id) ON DELETE RESTRICT,
    quantity INT NOT NULL,
    PRIMARY KEY (product_id, address_id)
);

CREATE TABLE IF NOT EXISTS warehouse.bookings (
    booking_id UUID PRIMARY KEY,
    order_id UUID NOT NULl UNIQUE,
    delivery_id UUID,
    state VARCHAR(20) NOT NULL DEFAULT 'ON_ASSEMBLY'
);

CREATE TABLE IF NOT EXISTS warehouse.booking_products (
    booking_id UUID REFERENCES warehouse.bookings(booking_id) ON DELETE CASCADE,
    product_id UUID,
    quantity INT NOT NULL,
    PRIMARY KEY (booking_id, product_id)
);