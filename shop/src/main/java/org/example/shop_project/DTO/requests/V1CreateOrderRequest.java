package org.example.shop_project.DTO.requests;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class V1CreateOrderRequest {
    
    @NotNull(message = "Orders cannot be null")
    @NotEmpty(message = "Orders cannot be empty")
    @Valid
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

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

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public String getDeliveryAddress() {
            return deliveryAddress;
        }

        public void setDeliveryAddress(String deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
        }

        public Long getTotalPriceCents() {
            return totalPriceCents;
        }

        public void setTotalPriceCents(Long totalPriceCents) {
            this.totalPriceCents = totalPriceCents;
        }

        public String getTotalPriceCurrency() {
            return totalPriceCurrency;
        }

        public void setTotalPriceCurrency(String totalPriceCurrency) {
            this.totalPriceCurrency = totalPriceCurrency;
        }

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }
    }

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

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getProductTitle() {
            return productTitle;
        }

        public void setProductTitle(String productTitle) {
            this.productTitle = productTitle;
        }

        public String getProductUrl() {
            return productUrl;
        }

        public void setProductUrl(String productUrl) {
            this.productUrl = productUrl;
        }

        public Long getPriceCents() {
            return priceCents;
        }

        public void setPriceCents(Long priceCents) {
            this.priceCents = priceCents;
        }

        public String getPriceCurrency() {
            return priceCurrency;
        }

        public void setPriceCurrency(String priceCurrency) {
            this.priceCurrency = priceCurrency;
        }
    }
}
