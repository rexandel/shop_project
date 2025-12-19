package org.example.shop.DTO.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
public class V1CreateOrderRequest {
    
    @NotNull(message = "Orders cannot be null")
    @NotEmpty(message = "Orders cannot be empty")
    @Valid
    private List<Order> orders;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Order {
        @NotNull(message = "CustomerId cannot be null")
        private Long customerId;
        
        @NotNull(message = "DeliveryAddress cannot be null")
        @NotEmpty(message = "DeliveryAddress cannot be empty")
        private String deliveryAddress;
        
        @NotNull(message = "TotalPriceCents cannot be null")
        private Long totalPriceCents;
        
        @NotNull(message = "TotalPriceCurrency cannot be null")
        @NotEmpty(message = "TotalPriceCurrency cannot be empty")
        private String totalPriceCurrency;
        
        @NotNull(message = "OrderItems cannot be null")
        @NotEmpty(message = "OrderItems cannot be empty")
        @Valid
        private List<OrderItem> orderItems;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        @NotNull(message = "ProductId cannot be null")
        private Long productId;
        
        @NotNull(message = "Quantity cannot be null")
        private Integer quantity;
        
        @NotNull(message = "ProductTitle cannot be null")
        @NotEmpty(message = "ProductTitle cannot be empty")
        private String productTitle;
        
        private String productUrl;
        
        @NotNull(message = "PriceCents cannot be null")
        private Long priceCents;
        
        @NotNull(message = "PriceCurrency cannot be null")
        @NotEmpty(message = "PriceCurrency cannot be empty")
        private String priceCurrency;
    }
}
