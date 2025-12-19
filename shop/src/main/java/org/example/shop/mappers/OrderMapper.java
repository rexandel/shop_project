package org.example.shop.mappers;

import org.example.shop.BLL.orders.models.OrderItemUnit;
import org.example.shop.BLL.orders.models.OrderUnit;
import org.example.shop.DAL.orders.entity.Order;
import org.example.shop.DAL.orders.entity.OrderItem;
import org.example.shop.DTO.requests.V1CreateOrderRequest;
import org.example.shop.DTO.responses.V1CreateOrderResponse;
import org.example.shop.DTO.responses.V1QueryOrdersResponse;
import org.example.shop.rabbit.message.OrderCreatedMessage;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // DTO -> Entity
    public Order toEntity(V1CreateOrderRequest.Order dto, OffsetDateTime now) {
        Order entity = new Order();
        entity.setCustomerId(dto.getCustomerId());
        entity.setDeliveryAddress(dto.getDeliveryAddress());
        entity.setTotalPriceCents(dto.getTotalPriceCents());
        entity.setTotalPriceCurrency(dto.getTotalPriceCurrency());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    public List<Order> toEntities(List<V1CreateOrderRequest.Order> dtos, OffsetDateTime now) {
        return dtos.stream()
                .map(dto -> toEntity(dto, now))
                .collect(Collectors.toList());
    }

    public OrderItem toEntity(V1CreateOrderRequest.OrderItem dto, Order order, OffsetDateTime now) {
        OrderItem entity = new OrderItem();
        entity.setOrder(order);
        entity.setProductId(dto.getProductId());
        entity.setQuantity(dto.getQuantity());
        entity.setProductTitle(dto.getProductTitle());
        entity.setProductUrl(dto.getProductUrl());
        entity.setPriceCents(dto.getPriceCents());
        entity.setPriceCurrency(dto.getPriceCurrency());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return entity;
    }

    // Entity -> BLL Model
    public OrderUnit toOrderUnit(Order entity) {
        OrderUnit unit = new OrderUnit();
        unit.setId(entity.getId());
        unit.setCustomerId(entity.getCustomerId());
        unit.setDeliveryAddress(entity.getDeliveryAddress());
        unit.setTotalPriceCents(entity.getTotalPriceCents());
        unit.setTotalPriceCurrency(entity.getTotalPriceCurrency());
        unit.setCreatedAt(entity.getCreatedAt());
        unit.setUpdatedAt(entity.getUpdatedAt());
        return unit;
    }

    public OrderItemUnit toOrderItemUnit(OrderItem entity) {
        OrderItemUnit unit = new OrderItemUnit();
        unit.setId(entity.getId());
        unit.setOrderId(entity.getOrder() != null ? entity.getOrder().getId() : null);
        unit.setProductId(entity.getProductId());
        unit.setQuantity(entity.getQuantity());
        unit.setProductTitle(entity.getProductTitle());
        unit.setProductUrl(entity.getProductUrl());
        unit.setPriceCents(entity.getPriceCents());
        unit.setPriceCurrency(entity.getPriceCurrency());
        unit.setCreatedAt(entity.getCreatedAt());
        unit.setUpdatedAt(entity.getUpdatedAt());
        return unit;
    }

    public OrderUnit toOrderUnit(Order entity, List<OrderItem> items) {
        OrderUnit unit = toOrderUnit(entity);
        if (items != null) {
            unit.setOrderItems(items.stream()
                    .map(this::toOrderItemUnit)
                    .collect(Collectors.toList()));
        }
        return unit;
    }

    public List<OrderUnit> toOrderUnits(List<Order> entities, List<OrderItem> items) {
        return entities.stream()
                .map(order -> {
                    List<OrderItem> orderItems = items.stream()
                            .filter(item -> item.getOrder().getId().equals(order.getId()))
                            .collect(Collectors.toList());
                    return toOrderUnit(order, orderItems);
                })
                .collect(Collectors.toList());
    }

    // Entity -> Response DTO
    public V1CreateOrderResponse.Order toCreateResponse(Order entity, List<OrderItem> items) {
        V1CreateOrderResponse.Order dto = new V1CreateOrderResponse.Order();
        dto.setId(entity.getId());
        dto.setCustomerId(entity.getCustomerId());
        dto.setDeliveryAddress(entity.getDeliveryAddress());
        dto.setTotalPriceCents(entity.getTotalPriceCents());
        dto.setTotalPriceCurrency(entity.getTotalPriceCurrency());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        if (items != null) {
            dto.setOrderItems(items.stream()
                    .map(this::toCreateResponseItem)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public V1CreateOrderResponse.OrderItem toCreateResponseItem(OrderItem entity) {
        V1CreateOrderResponse.OrderItem dto = new V1CreateOrderResponse.OrderItem();
        dto.setId(entity.getId());
        dto.setOrderId(entity.getOrder() != null ? entity.getOrder().getId() : null);
        dto.setProductId(entity.getProductId());
        dto.setQuantity(entity.getQuantity());
        dto.setProductTitle(entity.getProductTitle());
        dto.setProductUrl(entity.getProductUrl());
        dto.setPriceCents(entity.getPriceCents());
        dto.setPriceCurrency(entity.getPriceCurrency());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    // BLL Model -> Response DTO
    public V1QueryOrdersResponse.Order toQueryResponse(OrderUnit unit) {
        V1QueryOrdersResponse.Order dto = new V1QueryOrdersResponse.Order();
        dto.setId(unit.getId());
        dto.setCustomerId(unit.getCustomerId());
        dto.setDeliveryAddress(unit.getDeliveryAddress());
        dto.setTotalPriceCents(unit.getTotalPriceCents());
        dto.setTotalPriceCurrency(unit.getTotalPriceCurrency());
        dto.setCreatedAt(unit.getCreatedAt());
        dto.setUpdatedAt(unit.getUpdatedAt());

        if (unit.getOrderItems() != null) {
            dto.setOrderItems(unit.getOrderItems().stream()
                    .map(this::toQueryResponseItem)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public V1QueryOrdersResponse.OrderItem toQueryResponseItem(OrderItemUnit unit) {
        V1QueryOrdersResponse.OrderItem dto = new V1QueryOrdersResponse.OrderItem();
        dto.setId(unit.getId());
        dto.setOrderId(unit.getOrderId());
        dto.setProductId(unit.getProductId());
        dto.setQuantity(unit.getQuantity());
        dto.setProductTitle(unit.getProductTitle());
        dto.setProductUrl(unit.getProductUrl());
        dto.setPriceCents(unit.getPriceCents());
        dto.setPriceCurrency(unit.getPriceCurrency());
        dto.setCreatedAt(unit.getCreatedAt());
        dto.setUpdatedAt(unit.getUpdatedAt());
        return dto;
    }

    // Entity -> RabbitMQ Message
    public OrderCreatedMessage toMessage(Order entity, List<OrderItem> items) {
        OrderCreatedMessage message = new OrderCreatedMessage();
        message.setId(entity.getId());
        message.setCustomerId(entity.getCustomerId());
        message.setDeliveryAddress(entity.getDeliveryAddress());
        message.setTotalPriceCents(entity.getTotalPriceCents());
        message.setTotalPriceCurrency(entity.getTotalPriceCurrency());

        if (items != null) {
            message.setOrderItems(items.stream()
                    .filter(item -> item.getOrder().getId().equals(entity.getId()))
                    .map(this::toMessageItem)
                    .collect(Collectors.toList()));
        }
        return message;
    }

    public OrderCreatedMessage.OrderItemMessage toMessageItem(OrderItem entity) {
        OrderCreatedMessage.OrderItemMessage message = new OrderCreatedMessage.OrderItemMessage();
        message.setId(entity.getId());
        message.setProductId(entity.getProductId());
        message.setProductTitle(entity.getProductTitle());
        message.setProductUrl(entity.getProductUrl());
        message.setQuantity(entity.getQuantity());
        message.setPriceCents(entity.getPriceCents());
        message.setPriceCurrency(entity.getPriceCurrency());
        return message;
    }

    public List<OrderCreatedMessage> toMessages(List<Order> entities, List<OrderItem> items) {
        return entities.stream()
                .map(order -> {
                    List<OrderItem> orderItems = items.stream()
                            .filter(item -> item.getOrder().getId().equals(order.getId()))
                            .collect(Collectors.toList());
                    return toMessage(order, orderItems);
                })
                .collect(Collectors.toList());
    }
}

