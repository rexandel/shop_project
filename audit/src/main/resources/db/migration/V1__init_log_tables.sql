CREATE TABLE IF NOT EXISTS audit_log_order (
    id BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(255) NOT NULL,
    order_item_id VARCHAR(255),
    customer_id VARCHAR(255),
    order_status VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);