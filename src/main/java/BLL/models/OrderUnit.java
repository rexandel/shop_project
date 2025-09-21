package BLL.models;


import java.time.OffsetDateTime;
import java.util.List;

public class OrderUnit {
    private Long id;
    private Long customerId;
    private String deliveryAddress;
    private Long totalPriceCents;
    private String totalPriceCurrency;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<OrderItemUnit> orderItems;

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

    public List<OrderItemUnit> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemUnit> orderItems) {
        this.orderItems = orderItems;
    }
}
