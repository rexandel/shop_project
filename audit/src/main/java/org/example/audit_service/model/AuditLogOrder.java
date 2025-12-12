package org.example.audit_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_log_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "product_url")
    private String productUrl;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "total_price_cents")
    private Long totalPriceCents;

    @Column(name = "total_price_currency")
    private String totalPriceCurrency;

    @Column(name = "price_cents")
    private Long priceCents;

    @Column(name = "price_currency")
    private String priceCurrency;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
