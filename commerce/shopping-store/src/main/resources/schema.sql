CREATE SCHEMA IF NOT EXISTS "shopping_store";

-- Убедись, что расширение pgcrypto установлено (для gen_random_uuid)
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Создание таблицы
CREATE TABLE "shopping_store".products (
--    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id UUID PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    image_src TEXT,
    category TEXT NOT NULL,
    quantity_state TEXT NOT NULL,
    product_state TEXT NOT NULL
);