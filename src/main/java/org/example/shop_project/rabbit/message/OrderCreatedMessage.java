package org.example.shop_project.rabbit.message;

import java.util.List;

public class OrderCreatedMessage {
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

    public List<OrderItemMessage> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemMessage> orderItems) {
        this.orderItems = orderItems;
    }

    private Long id;
    private Long customerId;
    private String deliveryAddress;
    private Long totalPriceCents;
    private String totalPriceCurrency;
    private List<OrderItemMessage> orderItems;

    public static class OrderItemMessage {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
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

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
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

        private Long productId;
        private String productTitle;
        private String productUrl;
        private Integer quantity;
        private Long priceCents;
        private String priceCurrency;
    }
}