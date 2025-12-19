package org.example.audit_service.mappers;

import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.model.AuditLogOrder;
import org.example.audit_service.OrderCreatedMessage;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class AuditLogMapper {

    public AuditLogOrderRequest.LogOrder toLogOrder(OrderCreatedMessage message, OrderCreatedMessage.OrderItemMessage item) {
        return new AuditLogOrderRequest.LogOrder(
                message.getId(),
                item.getId(),
                message.getCustomerId(),
                "ORDER_CREATED"
        );
    }

    public AuditLogOrder toEntity(AuditLogOrderRequest.LogOrder dto) {
        OffsetDateTime now = OffsetDateTime.now();
        return AuditLogOrder.builder()
                .orderId(dto.getOrderId())
                .itemId(dto.getItemId())
                .customerId(dto.getCustomerId())
                .eventType(dto.getEventType())
                .createdAt(now)
                .updatedAt(now)
                .build();
    }
}

