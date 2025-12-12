-- Создаём таблицу заказов
CREATE TABLE IF NOT EXISTS orders (
                                      id BIGSERIAL NOT NULL PRIMARY KEY,
                                      customer_id BIGINT NOT NULL,
                                      delivery_address TEXT NOT NULL,
                                      total_price_cents BIGINT NOT NULL,
                                      total_price_currency TEXT NOT NULL,
                                      created_at TIMESTAMPTZ NOT NULL,
                                      updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_order_customer_id ON orders (customer_id);

-- Создаём таблицу элементов заказа
CREATE TABLE IF NOT EXISTS order_items (
                                           id BIGSERIAL NOT NULL PRIMARY KEY,
                                           order_id BIGINT NOT NULL,
                                           product_id BIGINT NOT NULL,
                                           quantity INT NOT NULL,
                                           product_title TEXT NOT NULL,
                                           product_url TEXT NOT NULL,
                                           price_cents BIGINT NOT NULL,
                                           price_currency TEXT NOT NULL,
                                           created_at TIMESTAMPTZ NOT NULL,
                                           updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_order_item_order_id ON order_items(order_id);

-- Создаём композитные типы
CREATE TYPE v1_order AS (
    id BIGINT,
    customer_id BIGINT,
    delivery_address TEXT,
    total_price_cents BIGINT,
    total_price_currency TEXT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
    );

CREATE TYPE v1_order_item AS (
    id BIGINT,
    order_id BIGINT,
    product_id BIGINT,
    quantity INT,
    product_title TEXT,
    product_url TEXT,
    price_cents BIGINT,
    price_currency TEXT,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
    );

