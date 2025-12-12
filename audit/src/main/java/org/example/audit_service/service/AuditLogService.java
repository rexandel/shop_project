package org.example.audit_service.service;

import lombok.RequiredArgsConstructor;
import org.example.audit_service.DAL.AuditLogOrderDAO;
import org.example.audit_service.DTO.AuditLogOrderRequest;
import org.example.audit_service.model.AuditLogOrder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogOrderDAO auditLogOrderDAO;

    public void logOrder(AuditLogOrderRequest.LogOrder logOrder) {
        AuditLogOrder log = AuditLogOrder.builder()
                .orderId(logOrder.getOrderId())
                .itemId(logOrder.getItemId())
                .customerId(logOrder.getCustomerId())
                .productId(logOrder.getProductId())
                .quantity(logOrder.getQuantity())
                .productTitle(logOrder.getProductTitle())
                .productUrl(logOrder.getProductUrl())
                .deliveryAddress(logOrder.getDeliveryAddress())
                .totalPriceCents(logOrder.getTotalPriceCents())
                .totalPriceCurrency(logOrder.getTotalPriceCurrency())
                .priceCents(logOrder.getPriceCents())
                .priceCurrency(logOrder.getPriceCurrency())
                .eventType(logOrder.getEventType())
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        auditLogOrderDAO.save(log);
    }
}