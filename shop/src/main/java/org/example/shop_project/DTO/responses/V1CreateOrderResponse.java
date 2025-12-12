package org.example.shop_project.DTO.responses;

import java.time.OffsetDateTime;
import java.util.List;

public class V1CreateOrderResponse {
    
    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static class Order {
        private Long id;
        private Long customerId;
        private String deliveryAddress;
        private Long totalPriceCents;
        private String totalPriceCurrency;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private List<OrderItem> orderItems;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

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

        public OffsetDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public OffsetDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }

        public List<OrderItem> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
        }
    }

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

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getOrderId() {
            return orderId;
        }

        public void setOrderId(Long orderId) {
            this.orderId = orderId;
        }

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

        public OffsetDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
        }

        public OffsetDateTime getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}
