CREATE TABLE IF NOT EXISTS audit_log_order (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    item_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    product_id BIGINT,
    quantity INT,
    product_title VARCHAR(255),
    product_url TEXT,
    delivery_address TEXT,
    total_price_cents BIGINT,
    total_price_currency VARCHAR(10),
    price_cents BIGINT,
    price_currency VARCHAR(10),
    event_type VARCHAR(50),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);