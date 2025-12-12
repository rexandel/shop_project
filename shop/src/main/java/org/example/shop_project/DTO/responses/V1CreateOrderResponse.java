package org.example.shop_project.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;
import java.util.List;

@Data
public class V1CreateOrderResponse {
    
    private List<Order> orders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {
        private Long id;
        private Long customerId;
        private String deliveryAddress;
        private Long totalPriceCents;
        private String totalPriceCurrency;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private List<OrderItem> orderItems;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private Long id;
        private Long orderId;
        private Long productId;
        private Integer quantity;
        private String productTitle;
        private String productUrl;
        private Long priceCents;
        private String priceCurrency;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }
}
