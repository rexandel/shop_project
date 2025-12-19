package org.example.shop.BLL.orders.models;

import lombok.Data;
import java.time.OffsetDateTime;

@Data
public class OrderItemUnit {
    private Long id;
    private Long orderId;
    private Long productId;
    private int quantity;
    private String productTitle;
    private String productUrl;
    private Long priceCents;
    private String priceCurrency;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
