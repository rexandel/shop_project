package org.example.shop.BLL.orders.models;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class OrderUnit {
    private Long id;
    private Long customerId;
    private String deliveryAddress;
    private Long totalPriceCents;
    private String totalPriceCurrency;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<OrderItemUnit> orderItems;
}
