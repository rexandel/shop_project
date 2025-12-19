package org.example.shop.rabbit.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedMessage extends BaseMessage {
    private Long id;
    private Long customerId;
    private String deliveryAddress;
    private Long totalPriceCents;
    private String totalPriceCurrency;
    private List<OrderItemMessage> orderItems;

    @Override
    public String getRoutingKey() {
        return "order.created";
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemMessage {
        private Long id;
        private Long productId;
        private String productTitle;
        private String productUrl;
        private Integer quantity;
        private Long priceCents;
        private String priceCurrency;
    }
}