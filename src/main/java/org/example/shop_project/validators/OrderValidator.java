package org.example.shop_project.validators;

import org.example.shop_project.DTO.requests.V1CreateOrderRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderValidator {

    public void validateOrder(V1CreateOrderRequest.Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        // Проверка CustomerId
        if (order.getCustomerId() == null || order.getCustomerId() <= 0) {
            throw new IllegalArgumentException("CustomerId must be greater than 0");
        }

        // Проверка DeliveryAddress
        if (order.getDeliveryAddress() == null || order.getDeliveryAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("DeliveryAddress cannot be empty");
        }

        // Проверка TotalPriceCents
        if (order.getTotalPriceCents() == null || order.getTotalPriceCents() <= 0) {
            throw new IllegalArgumentException("TotalPriceCents must be greater than 0");
        }

        // Проверка TotalPriceCurrency
        if (order.getTotalPriceCurrency() == null || order.getTotalPriceCurrency().trim().isEmpty()) {
            throw new IllegalArgumentException("TotalPriceCurrency cannot be empty");
        }

        // Проверка OrderItems
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("OrderItems cannot be null or empty");
        }

        // Валидация каждого OrderItem
        for (V1CreateOrderRequest.OrderItem item : order.getOrderItems()) {
            validateOrderItem(item);
        }

        // Проверка что TotalPriceCents равен сумме всех OrderItems.PriceCents * OrderItems.Quantity
        long calculatedTotal = order.getOrderItems().stream()
                .mapToLong(item -> item.getPriceCents() * item.getQuantity())
                .sum();

        if (!order.getTotalPriceCents().equals(calculatedTotal)) {
            throw new IllegalArgumentException("TotalPriceCents should be equal to sum of all OrderItems.PriceCents * OrderItems.Quantity");
        }

        // Проверка что все валюты в OrderItems одинаковы
        List<String> currencies = order.getOrderItems().stream()
                .map(V1CreateOrderRequest.OrderItem::getPriceCurrency)
                .distinct()
                .toList();

        if (currencies.size() != 1) {
            throw new IllegalArgumentException("All OrderItems.PriceCurrency should be the same");
        }

        // Проверка что валюта OrderItems совпадает с TotalPriceCurrency
        if (!order.getTotalPriceCurrency().equals(currencies.get(0))) {
            throw new IllegalArgumentException("OrderItems.PriceCurrency should be the same as TotalPriceCurrency");
        }
    }

    private void validateOrderItem(V1CreateOrderRequest.OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("OrderItem cannot be null");
        }

        // Проверка ProductId
        if (item.getProductId() == null || item.getProductId() <= 0) {
            throw new IllegalArgumentException("ProductId must be greater than 0");
        }

        // Проверка Quantity
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        // Проверка ProductTitle
        if (item.getProductTitle() == null || item.getProductTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("ProductTitle cannot be empty");
        }

        // Проверка PriceCents
        if (item.getPriceCents() == null || item.getPriceCents() <= 0) {
            throw new IllegalArgumentException("PriceCents must be greater than 0");
        }

        // Проверка PriceCurrency
        if (item.getPriceCurrency() == null || item.getPriceCurrency().trim().isEmpty()) {
            throw new IllegalArgumentException("PriceCurrency cannot be empty");
        }
    }
}
