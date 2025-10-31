CREATE SCHEMA IF NOT EXISTS commerce_payment;

CREATE TABLE IF NOT EXISTS commerce_payment.payments (
    payment_id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    products_cost DECIMAL NOT NULL,
    delivery_cost DECIMAL NOT NULL,
    total_cost DECIMAL NOT NULL,
    status VARCHAR(20) NOT NULL
)
